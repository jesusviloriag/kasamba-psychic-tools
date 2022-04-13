package com.kasamba.psychictools.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kasamba.psychictools.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppPromoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppPromo.class);
        AppPromo appPromo1 = new AppPromo();
        appPromo1.setId(1L);
        AppPromo appPromo2 = new AppPromo();
        appPromo2.setId(appPromo1.getId());
        assertThat(appPromo1).isEqualTo(appPromo2);
        appPromo2.setId(2L);
        assertThat(appPromo1).isNotEqualTo(appPromo2);
        appPromo1.setId(null);
        assertThat(appPromo1).isNotEqualTo(appPromo2);
    }
}
