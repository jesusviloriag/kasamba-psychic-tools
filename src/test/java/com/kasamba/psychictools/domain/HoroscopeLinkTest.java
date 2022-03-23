package com.kasamba.psychictools.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kasamba.psychictools.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoroscopeLinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoroscopeLink.class);
        HoroscopeLink horoscopeLink1 = new HoroscopeLink();
        horoscopeLink1.setId(1L);
        HoroscopeLink horoscopeLink2 = new HoroscopeLink();
        horoscopeLink2.setId(horoscopeLink1.getId());
        assertThat(horoscopeLink1).isEqualTo(horoscopeLink2);
        horoscopeLink2.setId(2L);
        assertThat(horoscopeLink1).isNotEqualTo(horoscopeLink2);
        horoscopeLink1.setId(null);
        assertThat(horoscopeLink1).isNotEqualTo(horoscopeLink2);
    }
}
