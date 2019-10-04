package br.com.leilaojunit.matematica;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculosMatematicosTest {

    @Test
    public void deveMultiplicarNumerosMaioresQue30() {
        CalculosMatematicos matematica = new CalculosMatematicos();
        assertEquals(50*4, matematica.contaMaluca(50));
    }

    @Test
    public void deveMultiplicarNumerosMaioresQue10EMenoresQue30() {
        CalculosMatematicos matematica = new CalculosMatematicos();
        assertEquals(20*3, matematica.contaMaluca(20));
    }

    @Test
    public void deveMultiplicarNumerosMenoresQue10() {
        CalculosMatematicos matematica = new CalculosMatematicos();
        assertEquals(5*2, matematica.contaMaluca(5));
    }

}
