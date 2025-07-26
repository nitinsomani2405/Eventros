package com.project.eventros.services.implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.project.eventros.domain.entities.QrCode;
import com.project.eventros.domain.entities.QrCodeStatusEnum;
import com.project.eventros.domain.entities.Ticket;
import com.project.eventros.exceptions.QrCodeGenerationException;
import com.project.eventros.exceptions.QrCodeNotFoundException;
import com.project.eventros.respositories.QrCodeRepository;
import com.project.eventros.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;
    private final QrCodeRepository qrCodeRepository;
    private final QRCodeWriter qrCodeWriter;
    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage(uniqueId);
            QrCode qrCode = new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            return qrCodeRepository.saveAndFlush(qrCode);
        }catch(WriterException|IOException ex) {
            throw new QrCodeGenerationException("failed to generate QR code", ex);
        }
    }
    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix=qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );

        BufferedImage image= MatrixToImageWriter.toBufferedImage(bitMatrix);
        try(ByteArrayOutputStream baos=new ByteArrayOutputStream()){
            ImageIO.write(image,"PNG",baos);
            byte[] imageBytes=baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }

    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode=qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)
                .orElseThrow(QrCodeNotFoundException::new);
        try{
            return Base64.getDecoder().decode(qrCode.getValue());
        }catch (IllegalArgumentException ex) {
            log.error("Invalid QR code",ex);
            throw new QrCodeNotFoundException();
        }
    }


}
