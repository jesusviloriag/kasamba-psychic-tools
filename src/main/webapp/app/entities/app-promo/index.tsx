import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppPromo from './app-promo';
import AppPromoDetail from './app-promo-detail';
import AppPromoUpdate from './app-promo-update';
import AppPromoDeleteDialog from './app-promo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppPromoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppPromoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppPromoDetail} />
      <ErrorBoundaryRoute path={match.url} component={AppPromo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AppPromoDeleteDialog} />
  </>
);

export default Routes;
