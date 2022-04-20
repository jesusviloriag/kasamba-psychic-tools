package com.kasamba.psychictools.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AppPromo.
 */
@Entity
@Table(name = "app_promo")
public class AppPromo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "banid", nullable = false)
    private String banid;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @ManyToOne(optional = false)
    @NotNull
    private App app;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppPromo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AppPromo title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public AppPromo text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public AppPromo date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBanid() {
        return this.banid;
    }

    public AppPromo banid(String banid) {
        this.setBanid(banid);
        return this;
    }

    public void setBanid(String banid) {
        this.banid = banid;
    }

    public Integer getRate() {
        return this.rate;
    }

    public AppPromo rate(Integer rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public App getApp() {
        return this.app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public AppPromo app(App app) {
        this.setApp(app);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppPromo)) {
            return false;
        }
        return id != null && id.equals(((AppPromo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppPromo{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            ", date='" + getDate() + "'" +
            ", banid='" + getBanid() + "'" +
            ", rate=" + getRate() +
            "}";
    }
}
