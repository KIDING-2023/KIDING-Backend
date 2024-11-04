package com.demo.KIDING.service;
import com.demo.KIDING.global.auth.VerificationCode;
import com.demo.KIDING.global.auth.VerificationCodeGenerator;
import com.demo.KIDING.global.auth.VerificationCodeRepository;
import com.demo.KIDING.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.demo.KIDING.global.common.BaseResponseStatus.VERIFICATION_CODE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.sms.api-key}")
    private String apiKey;
    @Value("${spring.sms.api-secret}")
    private String apiSecret;
    @Value("${spring.sms.provider}")
    private String smsProvider;
    @Value("${spring.sms.sender}")
    private String smsSender;

    private DefaultMessageService messageService;
    private final VerificationCodeRepository verificationCodeRepository;

    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                smsProvider
        );
    }

    public String sendVerificationMessage(String to, LocalDateTime sentAt) throws BaseException {
        Message message = new Message();
        message.setFrom(smsSender);
        message.setTo(to);

        VerificationCode verificationCode = VerificationCodeGenerator
                .generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        String text = verificationCode.generateCodeMessage();
        message.setText(text);

        messageService.sendOne(new SingleMessageSendingRequest(message));

        return "인증번호를 보냈습니다.";
    }

    public String verifyCode(String code, LocalDateTime verifiedAt) throws BaseException {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new BaseException(VERIFICATION_CODE_NOT_FOUND));

        if(verificationCode.isExpired(verifiedAt)){
            throw new BaseException(VERIFICATION_CODE_NOT_FOUND);
        }

        verificationCodeRepository.remove(verificationCode);

        return "정상 인증 되었습니다.";
    }

}
