package br.com.leilaojunit.matcher;

import br.com.leilaojunit.leilao.dominio.Lance;
import br.com.leilaojunit.leilao.dominio.Leilao;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class LeilaoMatcher extends TypeSafeMatcher<Leilao> {

    //	Para receber o Lance que procuraremos dentro do Leilão, criaremos
//	um construtor nesse matcher
    private final Lance lance;

    public LeilaoMatcher(Lance lance) {
        this.lance = lance;
    }

//		Precisamos sobrescrever dois métodos: boolean matchesSafely(Leilao item) e
//		void describeTo(Description description).

    //	    retorna verdadeiro caso o lance exista ou falso caso não exista.
    @Override
    protected boolean matchesSafely(Leilao item) {
        return item.getLances().contains(lance);
    }

    //	    descrição desse matcher
    public void describeTo(Description description) {
        description.appendText("leilao com lance " + lance.getValor());
    }

    //	    método que instanciará o matcher nos testes
    public static Matcher<Leilao> temUmLance(Lance lance) {
        return new LeilaoMatcher(lance);
    }

}
