package main.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import main.java.enums.Tag;
import main.java.enums.Type;
import main.java.models.DocModel;
import main.java.models.ItemModel;
import main.java.utils.FileCheckerUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class TlvJsonParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Charset CP866_CHARSET = Charset.forName("cp866");
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    private static final String FLOAT_DELIMITER = ".";
    private static final short TAG_ID_BYTES_LENGTH = 2;
    private static final short LENGTH_BYTES_LENGTH = 2;
    private static final String TARGET_FOLDER = "parse_folder" + FileSystems.getDefault().getSeparator();

    private String fromFile;
    private String toFile;

    public TlvJsonParser(String binaryFile, String jsonFile) throws IllegalArgumentException {
        binaryFile = TARGET_FOLDER + binaryFile;
        jsonFile = TARGET_FOLDER + jsonFile;
        if(FileCheckerUtils.isValidBinaryFile(binaryFile) && FileCheckerUtils.isValidJsonFileName(jsonFile)) {
            this.fromFile = binaryFile;
            this.toFile = jsonFile;
        } else {
            throw new IllegalArgumentException("Files for read binary data and save json data must have valid names: *.bin, *.json");
        }
    }

    public void parse() throws IOException  {
        List<DocModel> docModels = getDocModelsFromBinaryFile();
        if(!docModels.isEmpty()) {
            saveDocModelsToTextFile(docModels);
        }
    }

    protected List<DocModel> getDocModelsFromBinaryFile() throws IOException {
        List<DocModel> docModels = new ArrayList<>();
        Path pathFromFile = Path.of(fromFile);
        try(var in = Files.newInputStream(pathFromFile)) {
            parseTlvFromInputStream(in, new DocModel(), docModels);
        }
        return docModels;
    }

    protected void saveDocModelsToTextFile(List<DocModel> models) throws IOException {
        Path pathToFile = Path.of(toFile);
        try(var out = Files.newBufferedWriter(pathToFile)) {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            out.write(objectWriter.writeValueAsString(models));
        }
    }

    protected<T extends TypeApplicable> void parseTlvFromInputStream(InputStream in, T modelForFilling, List<T> list) throws IOException {
        byte[] bytes;
        Short tagId = null;
        Short length = null;
        while((bytes = in.readNBytes(getBytesLength(tagId, length))) != null && bytes.length != 0) {
            if(tagId == null) {
                tagId = getShortFromBytes(bytes);
            } else if(length == null) {
                length = getShortFromBytes(bytes);
            } else {
                Optional<Tag> tagOptional =  Tag.getById(tagId);
                if(tagOptional.isPresent()) {
                    if(Tag.TLV.equals(tagOptional.get())) {
                        parseTlvFromInputStream(new ByteArrayInputStream(bytes), new ItemModel(), ((DocModel)modelForFilling).getItems());
                    } else {
                        modelForFilling.applyValueByType(tagOptional.get(), getValue(tagOptional.get().getInputType(), length, bytes));
                    }
                }
                tagId = null;
                length = null;
            }
        }
        list.add(modelForFilling);
    }

    protected Object getValue(Type inputType, short length, byte[] bytes) {
        if(Objects.isNull(bytes) || bytes.length == 0) {
            return null;
        }
        switch (inputType) {
            case UINT32 -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES).put(new byte[Long.BYTES - length]).put(bytes);
                byteBuffer.position(0);
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(byteBuffer.getLong()), UTC_ZONE_ID).format(FORMATTER);
            }
            case STRING -> {
                return new String(bytes, CP866_CHARSET);
            }
            case VLN -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES).put(new byte[Long.BYTES - length]).put(bytes);
                byteBuffer.position(0);
                return byteBuffer.getLong();
            }
            case FVLN -> {
                //first byte is delimiter position
                ByteBuffer byteBufferScale = ByteBuffer.allocate(Integer.BYTES).put(new byte[Integer.BYTES - 1]).put(bytes[0]);
                byteBufferScale.position(0);
                int scale = byteBufferScale.getInt();
                ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES).put(new byte[Long.BYTES - (length - 1)]).put(Arrays.copyOfRange(bytes, 1, bytes.length));
                byteBuffer.position(0);
                String quantityWithoutDelimiter = String.valueOf(byteBuffer.getLong());
                StringBuilder quantityWithDelimiter = new StringBuilder(quantityWithoutDelimiter).insert(quantityWithoutDelimiter.length() - scale, FLOAT_DELIMITER);
                return new BigDecimal(quantityWithDelimiter.toString()).setScale(scale, RoundingMode.UNNECESSARY);
            }
            default -> {return null;}
        }
    }

    protected short getShortFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.position(0);
        return byteBuffer.getShort();
    }

    protected short getBytesLength(Short tagId, Short length) {
        return Objects.isNull(tagId) ? TAG_ID_BYTES_LENGTH : Objects.isNull(length) ? LENGTH_BYTES_LENGTH : length;
    }

}
