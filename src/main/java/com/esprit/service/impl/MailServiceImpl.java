package com.esprit.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.esprit.dto.exam.MailExamDTO;
import com.esprit.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Value("${spring.mail.username}")
	private String valueFrom;

	private final JavaMailSender javaMailSender;

	private final SpringTemplateEngine templateEngine;

	public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendEmail(List<MailExamDTO> mailExams) throws Exception {

		Map<String, List<MailExamDTO>> postsPerType = mailExams.stream()
				.collect(Collectors.groupingBy(MailExamDTO::getClassId));
		for (Entry<String, List<MailExamDTO>> entry : postsPerType.entrySet()) {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariable("data", entry.getValue());
			String html = templateEngine.process("exam-template", context);
			helper.setTo(entry.getValue().get(0).getEmail());
			helper.setText(html, true);
			helper.setSubject("Planning des épreuves");
			helper.setFrom(valueFrom);
			javaMailSender.send(message);
		}
	}

}