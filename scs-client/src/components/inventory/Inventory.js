import React from 'react';
import {Component} from 'react';
import ClusterSelectorAndTypes from "./ClusterSelectorAndTypes";
import "./css/Inventory.css"
import ClusterObjectManagement from "./ClusterObjectManagement";
import { Row, Col } from 'antd';

export default class Inventory extends Component {

    constructor(props) {
        super(props);
        this.state = {
            clusterId : null,
            curType   : null
        }
    }

    setClusterId(clusterId) {
        this.setState({
            clusterId : clusterId
        });
    }

    setCurType(curType) {
        this.setState({
            curType : curType
        });
    }

    render() {
        return (
            <div>
                <Row>
                    <Col span={6}>
                        <ClusterSelectorAndTypes
                            setClusterIdMethod={this.setClusterId.bind(this)}
                            setCurTypeMethod={this.setCurType.bind(this)}/>
                    </Col>
                    <Col span={18}>
                        <ClusterObjectManagement
                            clusterId={this.state.clusterId}
                            curType={this.state.curType}/>
                    </Col>
                </Row>
            </div>
        )
    }

}