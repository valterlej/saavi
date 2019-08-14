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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * Opção de resposta de uma questão de múltipla escolha.
 * 
 * @author Valter Estevam
 */
@Entity (name="opcao")
public class Opcao implements Serializable, Comparable<Opcao>{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idOpcao;
    
    /**
     * Texto da opção de resposta.
     */
    @NotNull(message = "Texto completo é obrigatório")
    @Size(min=1, max=200, message = "O texto deve possuir entre 1 e 200 caracteres.")
    @Column(name = "textocompleto", nullable = false, length = 200)
    private String textoCompleto;
    
    /**
     * Texto curto da opção de resposta utilizado em relatórios.
     */
    @NotNull(message = "Texto curto é obrigatório")
    @Size(min=1, max=20, message = "O texto deve possuir entre 1 e 20 caracteres.")
    @Column(name = "textocurto", nullable = false, length = 20)
    private String textoCurto;
        
    /**
     * Construtor padrão.
     */
    public Opcao() {
        idOpcao = 0;
        textoCurto = "";
        textoCompleto = "";
    }

    /**
     * Construtor.
     * 
     * @param idOpcao identificador único.
     * @param textoCompleto texto completo da opção.
     * @param textoCurto textoCurto da opção.     
     */
    public Opcao(int idOpcao, String textoCompleto, String textoCurto) {
        this.idOpcao = idOpcao;
        this.textoCurto = textoCurto;
        this.textoCompleto = textoCompleto;
    }

    /**
     * 
     * @return identificador único.
     */
    public int getIdOpcao() {
        return idOpcao;
    }

    /**
     * 
     * @param idOpcao identificador único.
     */
    public void setIdOpcao(int idOpcao) {
        this.idOpcao = idOpcao;
    }
    
    /**
     * 
     * @return texto completo da opção.
     */
    public String getTextoCompleto() {
        return textoCompleto;
    }

    /**
     * 
     * @param textoCompleto texto curto da opção.
     */
    public void setTextoCompleto(String textoCompleto) {
        this.textoCompleto = textoCompleto;
    }

    /**
     * 
     * @return textoCurto da opção.
     */
    public String getTextoCurto() {
        return textoCurto;
    }

    /**
     * 
     * @param textoCurto textoCurto da opção
     */
    public void setTextoCurto(String textoCurto) {
        this.textoCurto = textoCurto;
    }

    /**
     * Método utilizado para comparação entre entre objetos para fins de ordenação.
     * A comparação se dá pelo identificador.
     * 
     * @param o objeto a ser comparado.
     * @return 0 se forem iguais, menor que 0 se o atual é vem antes do comparado e maior do que zero de o objeto comparado vem antes do atual.
     */
    @Override
    public int compareTo(Opcao o) {
        if (this.idOpcao > o.getIdOpcao()){
            return 1;
        }else if (this.idOpcao < o.getIdOpcao()){
            return -1;
        }else{
            return 0;
        }
    }

    /**
     * Método usado para verificar a igualdade entre objetos.
     * A comparação é feita pelo id.
     * 
     * @param obj objeto a ser comparado.
     * @return true se forem iguais.
     */
    @Override
    public boolean equals(Object obj) {
        Opcao o = (Opcao) obj;
        return this.idOpcao == o.getIdOpcao();
    }

    /**
     * 
     * @return hash code para o identificador da opção.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.idOpcao;
        return hash;
    }   
    
}