import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Menu" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="cookie-bite" to="/fortune-cookie">
      Fortune Cookie
    </MenuItem>
    <MenuItem icon="star" to="/horoscope-link">
      Horoscope Link
    </MenuItem>
    <MenuItem icon="mobile" to="/app">
      App
    </MenuItem>
    <MenuItem icon="percent" to="/app-promo">
      App Promo
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
