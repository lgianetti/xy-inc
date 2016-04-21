package br.com.zup.exception;


/**
 * 
 * Exceção para retorno vazio
 * 
 * */
public class CEPException extends RuntimeException{
	
	public CEPException(String information, Throwable cause) {
		super("Informação " + information + " não localizada.", cause);
	}

}
