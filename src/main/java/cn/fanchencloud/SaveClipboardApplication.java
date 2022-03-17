package cn.fanchencloud;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.RenderedImage;
import java.io.*;

/**
 * @author chen
 */
public class SaveClipboardApplication {

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            if (null != arg) {
                if (isDirectory(new File(arg))) {
                    // 获取剪切板信息
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable transferable = clipboard.getContents(null);
                    if (null != transferable) {
                        // 文本类型数据
                        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            String text = null;
                            try {
                                text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                            } catch (UnsupportedFlavorException | IOException e) {
                                e.printStackTrace();
                            }
                            if (null != text) {
                                String filename = text.length() > 8 ? text.substring(0, 8) : text;
                                filename = filename.replace("?", "");
                                filename = filename.replace("*", "");
                                filename = filename.replace("/", "");
                                filename = filename.replace("\\", "");
                                filename = filename.replace("<", "");
                                filename = filename.replace(">", "");
                                filename = filename.replace(":", "");
                                filename = filename.replace("\"", "");
                                filename = filename.replace("|", "");
                                File textFile = new File(arg + File.separator + filename + ".txt");
                                if (textFile.exists()) {
                                } else {
                                    // 不存在则创建
                                    try {
                                        // 不存在则创建
                                        if (!textFile.createNewFile()) {
                                            System.exit(0);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // true,则追加写入text文本
                                BufferedWriter bufferedWriter = null;
                                try {
                                    bufferedWriter = new BufferedWriter(new FileWriter(textFile, true));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (bufferedWriter != null) {
                                    bufferedWriter.write(text);
                                    //换行
                                    bufferedWriter.write("\r\n");
                                    bufferedWriter.flush();
                                    bufferedWriter.close();
                                }
                            }
                        }
                        // 图片类型
                        else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                            Image image = null;
                            try {
                                image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                            } catch (UnsupportedFlavorException | IOException e) {
                                e.printStackTrace();
                            }
                            if (image != null) {
                                // 获取时间戳
                                long timeMillis = System.currentTimeMillis();
                                try {
                                    ImageIO.write((RenderedImage) image, "JPEG", new FileOutputStream(arg + File.separator + timeMillis + ".jpg"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean isDirectory(File file) {
        return null != file && file.isDirectory();
    }


}
