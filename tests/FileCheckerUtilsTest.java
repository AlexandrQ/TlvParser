import main.java.utils.FileCheckerUtils;
import org.junit.Assert;
import org.junit.Test;

public class FileCheckerUtilsTest {

    @Test
    public void isValidJsonFileName() {
        Assert.assertTrue(FileCheckerUtils.isValidJsonFileName("fileName.json"));

        Assert.assertFalse(FileCheckerUtils.isValidJsonFileName("fileName.txt"));
        Assert.assertFalse(FileCheckerUtils.isValidJsonFileName("fileName.bin"));
        Assert.assertFalse(FileCheckerUtils.isValidJsonFileName("fileNamejson"));
        Assert.assertFalse(FileCheckerUtils.isValidJsonFileName(""));
        Assert.assertFalse(FileCheckerUtils.isValidJsonFileName(null));
    }

    @Test
    public void isValidBinaryFile() {
        Assert.assertFalse(FileCheckerUtils.isValidBinaryFile("fileName.txt"));
        Assert.assertFalse(FileCheckerUtils.isValidBinaryFile("fileName.json"));
        Assert.assertFalse(FileCheckerUtils.isValidBinaryFile("fileNamejson"));
        Assert.assertFalse(FileCheckerUtils.isValidBinaryFile(""));
        Assert.assertFalse(FileCheckerUtils.isValidBinaryFile(null));
    }

    @Test
    public void isValidFilename() {
        Assert.assertTrue(FileCheckerUtils.isValidFilename("t.txt", "txt"));
        Assert.assertTrue(FileCheckerUtils.isValidFilename("t.bin", "bin"));

        Assert.assertFalse(FileCheckerUtils.isValidFilename("t.txt.txt", "txt"));
        Assert.assertFalse(FileCheckerUtils.isValidFilename("t.txt", "bin"));
        Assert.assertFalse(FileCheckerUtils.isValidFilename("filetxt", "txt"));
        Assert.assertFalse(FileCheckerUtils.isValidFilename("filetxt", ""));
        Assert.assertFalse(FileCheckerUtils.isValidFilename(".txt", "txt"));
        Assert.assertFalse(FileCheckerUtils.isValidFilename(null, null));
    }

    @Test
    public void isNotEmptyString() {
        Assert.assertTrue(FileCheckerUtils.isNotEmptyString("t"));
        Assert.assertTrue(FileCheckerUtils.isNotEmptyString("tttt.ttt"));
        Assert.assertFalse(FileCheckerUtils.isNotEmptyString(""));
        Assert.assertFalse(FileCheckerUtils.isNotEmptyString(null));
    }
}