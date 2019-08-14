/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.modelo.relatorio;

/*
 * Registro de uma tabela correspondente a uma questão aberta.
 *
 * @author Valter Estevam
 */
public class RegistroAberta extends Registro {

    /**
     * Resposta dada à questão.
     */
    private String resposta;

    /**
     * Construtor padrão.
     */
    public RegistroAberta() {
        super();
        resposta = "";
    }

    /**
     * Construtor.
     *
     * @param resposta resposta dada à questão.
     */
    public RegistroAberta(String resposta) {
        super();
        this.resposta = resposta;
    }

    /**
     * Construtor.
     *
     * @param resposta resposta dada à questão.
     * @param titulo título do registro.
     */
    public RegistroAberta(String resposta, String titulo) {
        super(titulo);
        this.resposta = resposta;
    }

    /**
     * Dada a coluna retorna o valor armazenado.
     *
     * @param coluna nome da coluna.
     * @return valor armazenado.
     */
    public String valorPorColuna(String coluna) {
        return resposta;
    }

    /**
     *
     * @return resposta dada à questão.
     */
    public String getResposta() {
        return resposta;
    }

    /**
     * 
     * @param resposta resposta dada à questão.
     */
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

}