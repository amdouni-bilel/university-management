package com.esprit.service.mapper.migration;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.esprit.dto.migration.DayMigration;

public class DayRowMapper implements RowMapper<DayMigration> {

	@Override
	public DayMigration mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DayMigration().dayId(rs.getString("CODEJOUR")).label(rs.getString("NOMJOUR"));
	}

}