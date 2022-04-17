package com.esprit.service.mapper.migration;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.esprit.dto.classes.CreateClassRequest;

public class ClassRowMapper implements RowMapper<CreateClassRequest> {

	@Override
	public CreateClassRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new CreateClassRequest().classId(rs.getString("CODE_CL")).label(rs.getString("LIBELLE_CL"))
				.category(rs.getString("CATEGORIE")).email(rs.getString("MAIL"))
				.speciality(rs.getString("CODE_SPECIALITE"));
	}

}