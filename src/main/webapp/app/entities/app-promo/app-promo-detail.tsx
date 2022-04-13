import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './app-promo.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AppPromoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const appPromoEntity = useAppSelector(state => state.appPromo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appPromoDetailsHeading">AppPromo</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{appPromoEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{appPromoEntity.title}</dd>
          <dt>
            <span id="text">Text</span>
          </dt>
          <dd>{appPromoEntity.text}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{appPromoEntity.date ? <TextFormat value={appPromoEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="banid">Banid</span>
          </dt>
          <dd>{appPromoEntity.banid}</dd>
          <dt>
            <span id="rate">Rate</span>
          </dt>
          <dd>{appPromoEntity.rate}</dd>
          <dt>App</dt>
          <dd>{appPromoEntity.app ? appPromoEntity.app.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/app-promo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app-promo/${appPromoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppPromoDetail;
