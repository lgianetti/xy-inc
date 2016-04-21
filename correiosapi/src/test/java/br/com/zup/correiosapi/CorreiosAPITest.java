package br.com.zup.correiosapi;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.zup.factory.ServiceFactory;
import br.com.zup.interfaces.Service;

public class CorreiosAPITest {
	
	@Test
	public void searchCEP(){
		Service buscaCEP = ServiceFactory.getService();
		CEP cep = buscaCEP.getCEP("08542340");
		assertEquals("Expression: ", "Rua Benedito Bastos Antunes", cep.getLogradouro());
	}
	
	@Test
	public void searchAddress(){
		Service buscaCEP = ServiceFactory.getService();
		List<CEP> cepList = buscaCEP.getAdress("Madri");
		
		CEP cep = new CEP("Rua Madri (C Ville IV)", "Serra dos Lagos (Jordanésia)", "Cajamar", "SP", "07781075");
		assertEquals("Expression: ", true, cepList.contains(cep));
	}

}
