import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HoroscopeLink from './horoscope-link';
import HoroscopeLinkDetail from './horoscope-link-detail';
import HoroscopeLinkUpdate from './horoscope-link-update';
import HoroscopeLinkDeleteDialog from './horoscope-link-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HoroscopeLinkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HoroscopeLinkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HoroscopeLinkDetail} />
      <ErrorBoundaryRoute path={match.url} component={HoroscopeLink} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HoroscopeLinkDeleteDialog} />
  </>
);

export default Routes;
