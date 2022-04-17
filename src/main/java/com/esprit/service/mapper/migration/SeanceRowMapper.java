package com.esprit.service.mapper.migration;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.esprit.dto.seance.CreateSeanceRequest;

public class SeanceRowMapper implements RowMapper<CreateSeanceRequest> {

	@Override
	public CreateSeanceRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new CreateSeanceRequest().seanceId(rs.getString("CODESEANCE"))
				.startHour(Integer.parseInt(rs.getString("HEUREDEB").replace("H:", "")))
				.endHour(Integer.parseInt(rs.getString("HEUREFIN").replace("H:", "")));
	}

}