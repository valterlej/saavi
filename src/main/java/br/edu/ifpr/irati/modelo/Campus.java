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
 * Campus da Instituição.
 *
 * @author Valter Estevam
 */
@Entity (name="campus")
public class Campus implements Serializable, Comparable<Campus>{
    
    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idCampus;
    
    /**
     * Nome completo do campus.
     */
    @NotNull(message = "Nome completo é obrigatório.")
    @Size(min=1, max=100, message = "O nome completo deve possuir entre 1 e 100 caracteres.")    
    @Column(name = "nomecompleto", nullable = false, length = 100)
    private String nomeCompleto;
    
    /**
     * Nome curto para uso nos relatórios.
     */
    @NotNull(message = "Nome curto é obrigatório.")
    @Size(min=1, max=25, message = "O nome curto deve possuir entre 1 e 25 caracteres.") 
    @Column(name = "nomecurto", nullable = false, length = 25)
    private String nomeCurto;

    /**
     * Construtor padrão.
     */
    public Campus() {              
        idCampus = 0;
        nomeCompleto = "";
        nomeCurto = "";
    }

    /**
     * Construtor.
     * 
     * @param idCampus identificador único.
     * @param nomeCompleto nome completo do campus.
     * @param nomeCurto nome curto para uso nos relatórios.
     */
    public Campus(int idCampus, String nomeCompleto, String nomeCurto) {
        this.idCampus = idCampus;
        this.nomeCompleto = nomeCompleto;
        this.nomeCurto = nomeCurto;
    }        
    
    /**
     * 
     * @return identificador único.
     */
    public int getIdCampus() {
        return idCampus;
    }

    /**
     * 
     * @param idCampus identificador único.
     */
    public void setIdCampus(int idCampus) {
        this.idCampus = idCampus;
    }

    /**
     * 
     * @return nome completo do campus.
     */
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    /**
     * 
     * @param nomeCompleto nome completo do campus.
     */
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    /**
     * 
     * @return nome curto utilizado nos relatórios.
     */
    public String getNomeCurto() {
        return nomeCurto;
    }
    
    /**
     * 
     * @param nomeCurto nome curto utilizado nos relatórios.
     */
    public void setNomeCurto(String nomeCurto) {
        this.nomeCurto = nomeCurto;
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
        Campus c = (Campus) obj;
        if (this.idCampus == c.getIdCampus()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Método utilizado para comparação entre entre objetos para fins de ordenação.
     * A comparação se dá pelo nome.
     * 
     * @param o objeto a ser comparado.
     * @return 0 se forem iguais, menor que 0 se o atual é vem antes do comparado e maior do que zero de o objeto comparado vem antes do atual.
     */
    @Override
    public int compareTo(Campus o) {
        return this.getNomeCompleto().compareTo(o.getNomeCompleto());
    }    
    
}