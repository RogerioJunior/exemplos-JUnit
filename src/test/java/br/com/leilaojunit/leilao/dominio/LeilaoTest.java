package br.com.leilaojunit.leilao.dominio;

import static org.junit.Assert.assertEquals;
import static br.com.leilaojunit.matcher.LeilaoMatcher.temUmLance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import br.com.leilaojunit.leilao.builder.CriadorDeLeilao;
import org.junit.Test;

public class LeilaoTest {

    @Test
    public void deveReceberUmLance() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .constroi();

        assertEquals(0, leilao.getLances().size());

        leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));

        assertEquals(1, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }

    // teste usando o matcher LeilaoMatcher
    @Test
    public void deveReceberUmLanceMatcher() {
        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .constroi();

        assertEquals(0, leilao.getLances().size());

        Lance lance = new Lance(new Usuario("Steve Jobs"), 2000);
        leilao.propoe(lance);


        assertThat(leilao.getLances().size(), equalTo(1));
        assertThat(leilao, temUmLance(lance));
    }

    @Test
    public void deveReceberVariosLances() {
        Leilao leilao = new CriadorDeLeilao()
                .para("Macbook Pro 15")
                .lance(new Usuario("Steve Jobs"), 2000)
                .lance(new Usuario("Steve Wozniak"), 3000)
                .constroi();

        assertEquals(2, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
        assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
        Usuario steveJobs = new Usuario("Steve Jobs");
        Leilao leilao = new CriadorDeLeilao()
                .para("Macbook Pro 15")
                .lance(steveJobs, 2000.0)
                .lance(steveJobs, 3000.0)
                .constroi();

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(steveJobs, 3000.0));

        assertEquals(1, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario() {
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario billGates = new Usuario("Bill Gates");

        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .lance(steveJobs, 2000)
                .lance(billGates, 3000)
                .lance(steveJobs, 4000)
                .lance(billGates, 5000)
                .lance(steveJobs, 6000)
                .lance(billGates, 7000)
                .lance(steveJobs, 8000)
                .lance(billGates, 9000)
                .lance(steveJobs, 10000)
                .lance(billGates, 11000)
                .lance(steveJobs, 12000)
                .constroi();

        // deve ser ignorado
        leilao.propoe(new Lance(steveJobs, 12000.0));

        assertEquals(10, leilao.getLances().size());
        assertEquals(11000.0, leilao.getLances().get(leilao.getLances().size()-1).getValor(), 0.00001);
    }

    @Test
    public void deveDobrarOUltimoLanceDado() {
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario billGates = new Usuario("Bill Gates");

        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .lance(steveJobs, 2000)
                .lance(billGates, 3000)
                .lance(steveJobs, 4000)
                .lance(billGates, 5000)
                .lance(steveJobs, 6000)
                .lance(billGates, 7000)
                .lance(steveJobs, 8000)
                .lance(billGates, 9000)
                .lance(steveJobs, 10000)
                .lance(billGates, 11000)
                .lance(steveJobs, 12000)
                .constroi();


        leilao.propoe(new Lance(steveJobs, 2000));
        leilao.propoe(new Lance(billGates, 3000));
        leilao.dobraLance(steveJobs);

        assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
    }

    @Test
    public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
        Usuario steveJobs = new Usuario("Steve Jobs");

        Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
                .constroi();

        leilao.dobraLance(steveJobs);

        assertEquals(0, leilao.getLances().size());
    }

}
