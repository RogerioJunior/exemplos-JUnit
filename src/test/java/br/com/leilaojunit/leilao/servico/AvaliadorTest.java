package br.com.leilaojunit.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import br.com.leilaojunit.leilao.builder.CriadorDeLeilao;
import br.com.leilaojunit.leilao.dominio.Lance;
import br.com.leilaojunit.leilao.dominio.Leilao;
import br.com.leilaojunit.leilao.dominio.Usuario;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AvaliadorTest {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario jose;
    private Usuario maria;

    // com o @before, o junit vai roda o metodo toda vez que rodar um @test, assim não precisamos chamar ele em todo @test
    @Before
    //public void setUp(){
    public void criaAvaliador() {
        this.leiloeiro = new Avaliador();
        this.joao = new Usuario("João");
        this.jose = new Usuario("José");
        this.maria = new Usuario("Maria");
    }

    //@BeforeClass executado uma vez, antes de todos os métodos de teste.
    @BeforeClass
    public static void testandoBeforeClass() {
        System.out.println("before class");
    }

    //@AfterClass executado uma vez, após a execução do último método de teste.
    @AfterClass
    public static void testandoAfterClass() {
        System.out.println("after class");
    }

    //@BeforeClass e @AfterClass podem ser bastante úteis quando temos algum recurso que precisa ser inicializado
    //apenas uma vez e que pode ser consumido por todos os métodos de teste sem a
    //necessidade de ser reinicializado.



    //@After são executados após a execução do método de teste.
    //Utilizamos métodos @After quando nossos testes consomem recursos que precisam ser finalizados.
    //Exemplos podem ser testes que acessam banco de dados, abrem arquivos, abrem sockets, e etc.
    @After
    public void finaliza() {
        System.out.println("fim");
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 1 Novo").constroi();

        leiloeiro.avalia(leilao);
    }

    // parte 1: cenario
    @Test
    public void deveEntenderLancesEmOrdemCrescente() {
        Leilao leilao = new CriadorDeLeilao().para("Psp 3 novo")
                .lance(joao, 250.00)
                .lance(jose, 300.00)
                .lance(maria, 400.00)
                .constroi();

        // parte 2: ação
        leiloeiro.avalia(leilao);

        double maiorEsperado = 400;
        double menorEsperado = 250;

        // parte 3: validação
        // projeto Hamcrest - contem instruções como assertThat(codigo teste mais legivel)
        // para usalo colocar no buildpath e ordem de exportação antes do Junit
        // https://code.google.com/archive/p/hamcrest/wikis/Tutorial.wiki
        assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
        //assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);
    }

    // parte 1: cenario
    @Test
    public void deveEntenderLeilaocomApenasUmLance() {
        Leilao leilao = new CriadorDeLeilao().para("Psp 3 novo")
                .lance(joao, 1000.0)
                .constroi();

        // parte 2: ação
        leiloeiro.avalia(leilao);

        double maiorEsperado = 1000.0;
        double menorEsperado = 1000.0;

        // parte 3: validação
        assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);
    }

    @Test
    public void deveEncontrarOsTresMaioresLances() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 200.0)
                .lance(maria, 300.0)
                .lance(joao, 400.0)
                .lance(maria, 500.0)
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(3, maiores.size());

        // hasItems recebe uma lista de itens e garante que esses itens existem na lista que passamos.
        //Hamcrest fara uso do metodo equals( que não existia na classe lance)
        assertThat(maiores, hasItems(
                new Lance(maria, 300),
                new Lance(joao, 400),
                new Lance(maria, 500)));

//        assertEquals(500.0, maiores.get(0).getValor(), 0.00001);
//        assertEquals(400.0, maiores.get(1).getValor(), 0.00001);
//        assertEquals(300.0, maiores.get(2).getValor(), 0.00001);
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 200.0)
                .lance(maria, 450.0)
                .lance(joao, 120.0)
                .lance(maria, 700.0)
                .lance(joao, 630.0)
                .lance(maria, 230.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 400.0)
                .lance(maria, 300.0)
                .lance(joao, 200.0)
                .lance(maria, 100.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertEquals(400.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(100.0, leiloeiro.getMenorLance(), 0.0001);
    }

    @Test
    public void deveDevolverTodosLancesCasoNaoHajaNoMinimo3() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 100.0)
                .lance(maria, 200.0)
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(2, maiores.size());
        assertEquals(200, maiores.get(0).getValor(), 0.00001);
        assertEquals(100, maiores.get(1).getValor(), 0.00001);
    }

    //Quando temos tratamento de exceção no metodo a ser testado e o teste tem que passar quando a exceção for lançada
    //usamos o atributo expected pertencente a anotação @test
    @Test(expected=RuntimeException.class)
    public void deveDevolverListaVaziaCasoNaoHajaLances() {
        Leilao leilao = new CriadorDeLeilao()
                .para("Playstation 3 Novo")
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(0, maiores.size());
    }


}
