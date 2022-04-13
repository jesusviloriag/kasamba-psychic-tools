import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import App from './app';
import AppDetail from './app-detail';
import AppUpdate from './app-update';
import AppDeleteDialog from './app-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppDetail} />
      <ErrorBoundaryRoute path={match.url} component={App} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AppDeleteDialog} />
  </>
);

export default Routes;
