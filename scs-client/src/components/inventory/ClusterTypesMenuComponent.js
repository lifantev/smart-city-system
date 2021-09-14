import React, {Component} from "react";
import { Menu, Button } from 'antd';
import {MenuUnfoldOutlined, MenuFoldOutlined} from '@ant-design/icons';
import "./css/ClusterTypesMenuComponent.css"

export default class ClusterTypesMenuComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            collapsed: false,
        };
    }

    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };


    render() {
        if (this.props.curClusterId === "-1") {
            return (
                <div> </div>
            )
        }
        else if (!this.props.isLoadedModels) {
            return (
                <div>Loading types...</div>
            )
        }
        else if (this.props.error) {
            return (
                <div>Error: {this.props.error.message}</div>
            )
        }
        else {
            return (
                <div className="Types">
                    <Button type="primary" onClick={this.toggleCollapsed} style={{marginBottom: 10}}>
                        Типы Кластера: {React.createElement(this.state.collapsed ? MenuUnfoldOutlined : MenuFoldOutlined)}
                    </Button>

                    <Menu mode="inline"
                          theme="dark"
                          onSelect={this.onChangeType}
                          inlineCollapsed={this.state.collapsed}>
                        {this.props.models.map(item => (
                            <Menu.Item key={item.type}>{item['displayed-name']}</Menu.Item>
                        ))}
                    </Menu>
                </div>
            );
        }
    }

    onChangeType = (e) => {
        this.setState({
            selectedType : e.key
        });
        this.props.setCurTypeMethod(e.key);
        window.history.pushState(null, null, "/config/" + this.props.curClusterId + "/" + e.key);
    }
}