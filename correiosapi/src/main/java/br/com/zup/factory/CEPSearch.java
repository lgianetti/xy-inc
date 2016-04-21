package br.com.zup.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.zup.correiosapi.CEP;
import br.com.zup.exception.CEPException;
import br.com.zup.exception.ServiceException;
import br.com.zup.interfaces.Service;


/**
 * Requisição via GET, serviço atua como façada da seguinte URL dos Correios:
 * 
 * http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm
 */
public class CEPSearch implements Service{
	
	private static final String CLEAR = "";
	private static final String HIFEN = "-";
	private static final String SPACE = "\u00a0";
	private static final String PIPE = "/";
	private static final int NUM = 3;
	private static final int LOGRADOURO = 0;
	private static final int BAIRRO = 1;
	private static final int LOCALIDADE = 2;
	private static final int UF = 2;	
	
	
	private static final String URL = "http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm";


	public CEP getCEP(String field) {
		Elements rows = search(field);
		for (Element row : rows) {
			CEP cep = this.getCepObject(row);
			return cep;
		}
		throw new CEPException(field, null);
	}

	public List<CEP> getAdress(String cepNumber) {
		Elements rows = search(cepNumber);
		List<CEP> ceps = new ArrayList<CEP>(rows.size());
		for (Element row : rows) {
			CEP cep = this.getCepObject(row);
			ceps.add(cep);
		}
		return ceps;
	}
	
	private CEP getCepObject(Element row) {
		Elements cols = row.getElementsByTag("td");
		CEP cep = new CEP(this.getLogradouro(cols), this.getBairro(cols), this.getLocalidade(cols), this.getUf(cols), this.getCepNumber(cols));
		return cep;
	}

	
	private String getUf(Elements cols) {
		String cidadeEstado = cols.get(UF).text().replace(SPACE, CLEAR);
		return cidadeEstado.split(PIPE)[1];
	}

	private String getLocalidade(Elements cols) {
		String cidadeEstado = cols.get(LOCALIDADE).text().replace(SPACE, CLEAR);
		return cidadeEstado.split(PIPE)[0];
	}

	private String getBairro(Elements cols) {
		return cols.get(BAIRRO).text().replace(SPACE, CLEAR);
	}

	private String getLogradouro(Elements cols) {
		return cols.get(LOGRADOURO).text().replace(SPACE, CLEAR);
	}

	private String getCepNumber(Elements cols) {
		String num = cols.get(NUM).text().replace(SPACE, CLEAR);
		return num.contains(HIFEN) ? num.replace(HIFEN, CLEAR) : num;
	}

	private Elements search(String q) {
		try {
			Document doc = Jsoup.connect(URL)
					.header("Content-Type","application/x-www-form-urlencoded;charset=ISO-8859-1")
					.data("relaxation", q)
					.data("tipoCEP", "ALL")
					.data("semelhante", "N")
					.post();
			Elements rows = doc.select("table.tmptabela tr:not(:first-child)");
			return rows;
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

}
