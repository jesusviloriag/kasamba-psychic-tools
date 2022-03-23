package com.kasamba.psychictools.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kasamba.psychictools.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FortuneCookieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FortuneCookie.class);
        FortuneCookie fortuneCookie1 = new FortuneCookie();
        fortuneCookie1.setId(1L);
        FortuneCookie fortuneCookie2 = new FortuneCookie();
        fortuneCookie2.setId(fortuneCookie1.getId());
        assertThat(fortuneCookie1).isEqualTo(fortuneCookie2);
        fortuneCookie2.setId(2L);
        assertThat(fortuneCookie1).isNotEqualTo(fortuneCookie2);
        fortuneCookie1.setId(null);
        assertThat(fortuneCookie1).isNotEqualTo(fortuneCookie2);
    }
}
