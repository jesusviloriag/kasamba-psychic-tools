import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FortuneCookie from './fortune-cookie';
import HoroscopeLink from './horoscope-link';
import App from './app';
import AppPromo from './app-promo';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}fortune-cookie`} component={FortuneCookie} />
      <ErrorBoundaryRoute path={`${match.url}horoscope-link`} component={HoroscopeLink} />
      <ErrorBoundaryRoute path={`${match.url}app`} component={App} />
      <ErrorBoundaryRoute path={`${match.url}app-promo`} component={AppPromo} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
