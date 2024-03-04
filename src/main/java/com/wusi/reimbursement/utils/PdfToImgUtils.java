package com.wusi.reimbursement.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ Description   :  将Pdf转为图片
 * @ Author        :  wusi
 * @ CreateDate    :  2021/3/19$ 16:12$
 */
public class PdfToImgUtils {
    public static void pdf2Image(String PdfFilePath, String dstImgFolder, int dpi,int flag,String fileName) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            String imagePDFName = fileName;
            String imgFolderPath = null;
            if (dstImgFolder.equals("")) {
                imgFolderPath = imgPDFPath ;
            } else {
                imgFolderPath = dstImgFolder;
            }

            if (createDirectory(imgFolderPath)) {
                pdDocument = PDDocument.load(file);

                PDFRenderer renderer = new PDFRenderer(pdDocument);
                int pages = pdDocument.getNumberOfPages();
                if(flag > 0) {//大于0则打印具体页数
                    if(flag<pages) {
                        pages = flag;
                    }
                }

                StringBuffer imgFilePath = null;
                for (int i = 0; i < pages; i++) {
                    String imgFilePathPrefix = imgFolderPath+File.separator + imagePDFName;
                    imgFilePath = new StringBuffer();
                    imgFilePath.append(imgFilePathPrefix);
                    //imgFilePath.append("-");
                    //imgFilePath.append(String.valueOf(i + 1));
                    imgFilePath.append(".png");
                    File dstFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageIO.write(image, "png", dstFile);
                }
                System.out.println("success");
            } else {
                System.out.println("error:" + "creat" + imgFolderPath + "wrong");
            }

        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
    }

    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }
    public static void main(String[] args) {
        pdf2Image("C:\\excel\\home.pdf","C:\\excel\\",300,1,"test");
    }
}
