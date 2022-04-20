import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './horoscope-link.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const HoroscopeLinkDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const horoscopeLinkEntity = useAppSelector(state => state.horoscopeLink.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="horoscopeLinkDetailsHeading">HoroscopeLink</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{horoscopeLinkEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{horoscopeLinkEntity.url}</dd>
        </dl>
        <Button tag={Link} to="/horoscope-link" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/horoscope-link/${horoscopeLinkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HoroscopeLinkDetail;
