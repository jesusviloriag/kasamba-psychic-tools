import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FortuneCookie from './fortune-cookie';
import FortuneCookieDetail from './fortune-cookie-detail';
import FortuneCookieUpdate from './fortune-cookie-update';
import FortuneCookieDeleteDialog from './fortune-cookie-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FortuneCookieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FortuneCookieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FortuneCookieDetail} />
      <ErrorBoundaryRoute path={match.url} component={FortuneCookie} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FortuneCookieDeleteDialog} />
  </>
);

export default Routes;
