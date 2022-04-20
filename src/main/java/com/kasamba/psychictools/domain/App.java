package com.kasamba.psychictools.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A App.
 */
@Entity
@Table(name = "app")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "codename", nullable = false)
    private String codename;

    @Column(name = "banid_android")
    private String banidAndroid;

    @Column(name = "banid_ios")
    private String banidIos;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public App id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public App name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodename() {
        return this.codename;
    }

    public App codename(String codename) {
        this.setCodename(codename);
        return this;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getBanidAndroid() {
        return this.banidAndroid;
    }

    public App banidAndroid(String banidAndroid) {
        this.setBanidAndroid(banidAndroid);
        return this;
    }

    public void setBanidAndroid(String banidAndroid) {
        this.banidAndroid = banidAndroid;
    }

    public String getBanidIos() {
        return this.banidIos;
    }

    public App banidIos(String banidIos) {
        this.setBanidIos(banidIos);
        return this;
    }

    public void setBanidIos(String banidIos) {
        this.banidIos = banidIos;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public App logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public App logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof App)) {
            return false;
        }
        return id != null && id.equals(((App) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "App{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", codename='" + getCodename() + "'" +
            ", banidAndroid='" + getBanidAndroid() + "'" +
            ", banidIos='" + getBanidIos() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
