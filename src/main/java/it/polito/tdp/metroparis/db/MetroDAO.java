package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroparis.model.CoppiaFermate;
import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM tdp.fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	public List<Linea> getAllLinee() {
		final String sql = "SELECT id_linea, nome, velocita, intervallo FROM tdp.linea ORDER BY nome ASC";

		List<Linea> linee = new ArrayList<Linea>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Linea f = new Linea(rs.getInt("id_linea"), rs.getString("nome"), rs.getDouble("velocita"),
						rs.getDouble("intervallo"));
				linee.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee;
	}

	public boolean fermateConnesse(Fermata fp, Fermata fa) {
		String sql = "SELECT COUNT(*) as C "
				+ "FROM tdp.connessione "
				+ "WHERE id_stazp = ? AND id_staza = ? ";		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fp.getIdFermata());
			st.setInt(2, fa.getIdFermata());
			st.executeQuery();
			ResultSet res = st.getResultSet();
			res.first();
			int linee = res.getInt("C");			
			st.close();
			conn.close();
			return linee >=1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Fermata> fermateSuccessive(Fermata fp, Map<Integer, Fermata> fermataIdMap){		
		List<Fermata> risult = new ArrayList<>();
		String sql = "SELECT DISTINCT id_staza "
				+ "FROM tdp.connessione "
				+ "WHERE id_stazp = ? ";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fp.getIdFermata());				
			ResultSet res = st.executeQuery();			
			while(res.next()) {
				risult.add(fermataIdMap.get(res.getInt("id_staza")));
			}						
			st.close();
			conn.close();
			return risult;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return risult;		
	}

	public List<CoppiaFermate> coppieFermate(Map<Integer, Fermata> fermateIdMap) {
		
		List<CoppiaFermate> result = new ArrayList<>();
		String sql = "SELECT DISTINCT id_stazp, id_staza "
				+ "FROM tdp.connessione";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
							
			ResultSet res = st.executeQuery();			
			while(res.next()) {
				
				CoppiaFermate c = new CoppiaFermate( fermateIdMap.get(res.getInt("id_stazp"))
													,fermateIdMap.get(res.getInt("id_staza"))
													);
				result.add(c);
						
			}						
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}

}
