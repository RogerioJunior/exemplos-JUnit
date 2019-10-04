package br.com.leilaojunit.leilao.builder;

import br.com.leilaojunit.leilao.dominio.Lance;
import br.com.leilaojunit.leilao.dominio.Leilao;
import br.com.leilaojunit.leilao.dominio.Usuario;

public class CriadorDeLeilao {
    //classe criar cenarios para o test "test data builder"

        private Leilao leilao;

        public CriadorDeLeilao() { }

        public CriadorDeLeilao para(String descricao) {
            this.leilao = new Leilao(descricao);
            return this;
        }

        public CriadorDeLeilao lance(Usuario usuario, double valor) {
            leilao.propoe(new Lance(usuario, valor));
            return this;
        }

        public Leilao constroi() {
            return leilao;
        }

}
