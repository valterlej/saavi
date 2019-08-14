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
package br.edu.ifpr.irati.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Resposta de questão aberta.
 * 
 * @author Valter Estevam
 */
@Entity(name = "respostaaberta")
@PrimaryKeyJoinColumn(name = "idResposta")
public class RespostaAberta extends Resposta implements Serializable{
    
    /**
     * Texto da resposta.
     */
    @Column(name = "nomecompleto", nullable = true, length = 500)
    private String texto;

    /**
     * Construtor padrão.
     */
    public RespostaAberta() {
        super();
    }

    /**
     * Construtor
     * 
     * @param texto texto da resposta.
     */
    public RespostaAberta(String texto) {
        super();
        this.texto = texto;
    }

    /**
     * Construtor.
     * 
     * @param texto texto da resposta.
     * @param idResposta identificador único.
     * @param dataHora data e hora da resposta.
     * @param questao questão respondida.
     * @param entrevistado entrevistado que respondeu.
     */
    public RespostaAberta(String texto, int idResposta, Date dataHora, Questao questao, Entrevistado entrevistado) {
        super(idResposta, dataHora, questao, entrevistado);
        this.texto = texto;
    }
          
    /**
     * 
     * @return texto da resposta.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * 
     * @param texto texto da resposta.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
}