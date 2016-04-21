package br.com.zup.decorator;

import br.com.zup.interfaces.Service;


/**
 * Acrescentar responsabilidades a um objeto em tempo de execução
 * Provem uma alternativa flexível ao uso de subclasses para se estender a funcionalidade de uma classe.
 **/

public abstract class AbstractServiceDecorator implements Service {
	
	protected Service decorated;
	
	
	public AbstractServiceDecorator(Service decorated) {
		super();
		this.decorated = decorated;
	}

}
