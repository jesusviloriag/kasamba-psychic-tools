import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './app.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AppDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const appEntity = useAppSelector(state => state.app.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appDetailsHeading">App</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{appEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{appEntity.name}</dd>
          <dt>
            <span id="codename">Codename</span>
          </dt>
          <dd>{appEntity.codename}</dd>
          <dt>
            <span id="banidAndroid">Banid Android</span>
          </dt>
          <dd>{appEntity.banidAndroid}</dd>
          <dt>
            <span id="banidIos">Banid Ios</span>
          </dt>
          <dd>{appEntity.banidIos}</dd>
          <dt>
            <span id="logo">Logo</span>
          </dt>
          <dd>
            {appEntity.logo ? (
              <div>
                {appEntity.logoContentType ? (
                  <a onClick={openFile(appEntity.logoContentType, appEntity.logo)}>
                    <img src={`data:${appEntity.logoContentType};base64,${appEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {appEntity.logoContentType}, {byteSize(appEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/app" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app/${appEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppDetail;
