package com.esprit.service;

import java.util.List;

import com.esprit.dto.exam.MailExamDTO;

public interface MailService {
	
	void sendEmail(List<MailExamDTO> mailExamDTO) throws Exception;

}