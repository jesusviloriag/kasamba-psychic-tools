import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p id="BottomText" className="nav-link">
          <b>
            Psychics are not employees or representatives of Kasamba. Use of this site is subject to the{' '}
            <a href="javascript:openHelpPopUp('690')" id="lnkTermsOfUse">
              <b>Terms of Use </b>
            </a>{' '}
            |{' '}
            <a href="https://www.kasamba.com/lp/privacy-policy/" id="lnkPrivacyPolicy">
              <b>Privacy Policy</b>{' '}
            </a>{' '}
            |{' '}
            <a href="javascript:openHelpPopUp('550')" id="lnkDisclaimer">
              <b>Disclaimer</b>{' '}
            </a>
          </b>{' '}
          <br />
          Address: 5, 475 10th Ave, New York, NY 10018, United States - Kasamba Psychics.
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
