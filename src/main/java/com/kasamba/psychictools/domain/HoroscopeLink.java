package com.kasamba.psychictools.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HoroscopeLink.
 */
@Entity
@Table(name = "horoscope_link")
public class HoroscopeLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HoroscopeLink id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public HoroscopeLink url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoroscopeLink)) {
            return false;
        }
        return id != null && id.equals(((HoroscopeLink) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoroscopeLink{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
