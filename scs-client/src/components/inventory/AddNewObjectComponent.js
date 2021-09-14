import React, {Component} from 'react'
import {Modal, Button, Select, notification, Col, Space, Form} from "antd";
import {Input, Row} from "antd";
import {GetAxios} from "../GetAxios";

const { Option } = Select;
const { TextArea } = Input;

const openNotificationWithIcon = (type, message, description) => {
    notification[type]({
        message: message,
        description:
            description,
    });
};

export default class AddNewObjectComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            models : [],
            isLoadedModels : false,
            error : null,
            shouldReloadModels : true,
        }
    }

    render() {
        return(
            <Modal
                visible={this.props.visible}
                title="Add New Object"
                onOk={this.closeWindow}
                onCancel={this.closeWindow}
                width={1000}
                footer={[
                    <Button key="cancel" onClick={this.closeWindow}>
                        Cancel
                    </Button>,
                    <Button htmlType="submit" key="submit" type="primary" loading={this.props.loading} onClick={this.addNewObject.bind(this)}>
                        Submit
                    </Button>
                ]}
            >
                <Form
                    name="addNewObjectForm"
                    onFinish={this.onFinish.bind(this)}
                    onFinishFailed={this.onFinishFailed.bind(this)}
                    autoComplete="off"
                >
                    <Form.Item
                        label="Type of object"
                        name="type"
                        rules={[
                            {
                                required: true,
                                message: 'Please, choose one of types!',
                            },
                        ]}
                    >
                        <Select loading={!this.state.isLoadedModels} style={{ width: "50%" }} onChange={this.handleChangeType}>
                            {this.state.models.map(item => (
                                <Option value={item.type}>{item['displayed-name']}</Option>
                            ))}
                        </Select>
                    </Form.Item>
                    {this.showDefaultFields()}
                    <div>
                        {this.state.models.map(model => {
                            if (model.type === this.state.typeOfNewObject) {
                                return model.attributes.map(attr => (
                                    <Form.Item
                                        label={attr['displayed-name']}
                                        name={attr.name}
                                    >
                                        <Input name={attr.name} onChange={this.onChangeInput}/>
                                    </Form.Item>
                                ))
                            }
                        })}
                    </div>
                    <Form.Item>
                        <Space size="middle">
                            <Button htmlType="submit" key="submit" type="primary" loading={this.props.loading}>
                                Submit
                            </Button>
                            <Button key="cancel" onClick={this.closeWindow} htmlType="button">
                                Cancel
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Modal>
        )
    }

    onFinish() {
        this.addNewObject();
    }

    onFinishFailed() {
    }

    onChangeInput = (e) => {
        const stateObject = function () {
            const returnObj = {};
            if (this.state[e.target.name] == null) {
                returnObj[e.target.name] = e.target.value;
            } else {
                returnObj[e.target.name] = e.target.value;
            }
            return returnObj;
        };
        this.setState(stateObject)
    }

    closeWindow = () => {
        this.setState({shouldReloadModels:true});
        this.props.setVisibleModal(false);
    }

    handleChangeType = (type) => {
        if (type === "-1") {
            this.setState({typeOfNewObject : null})
        }
        else {
            this.setState({typeOfNewObject:type})
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.shouldReloadModels) {
            this.updateTypesOfCluster();
            this.setState({shouldReloadModels : false})
        }
    }

    findElement = (type, arr) => {
        return arr.filter(item => {
            return item.type === type
        })
    };

    addNewObject() {
        this.props.setLoadingModal(true);

        let newObject = {};
        newObject["shard-id"] = this.props.curClusterId;
        newObject["type"] = this.state.typeOfNewObject;
        newObject["name"] = this.state["name"];
        newObject["description"] = this.state["description"];
        newObject["geo-pos-x"] = this.state["geo-pos-x"];
        newObject["geo-pos-y"] = this.state["geo-pos-y"];
        newObject["parameters"] = {};

        this.findElement(this.state.typeOfNewObject, this.state.models)[0].attributes.map(item => {
            newObject["parameters"][item.name] = this.state[item.name];
        })

        GetAxios().post(
            "/api/v1/data/",
            JSON.stringify(newObject),
            {headers : {'Content-Type' : 'application/json'}})
            .then(res => {
                if (res.status !== 200) {
                    this.props.setLoadingModal(false);
                    openNotificationWithIcon("error", "status " + res.status, "");
                }
                else {
                    this.props.setLoadingModal(false);
                    this.props.setVisibleModal(false);
                    this.props.updateVirginData();
                    openNotificationWithIcon("info", "OK", "New object was added!")
                }
                return res.data;
            })
            .catch(
                error => {
                    this.props.setLoadingModal(false);
                    this.setState({
                        error
                    });
                    openNotificationWithIcon("error", error.message, "Can't add new object!")
                }
            )
    }

    updateTypesOfCluster() {
        GetAxios().get("/api/v1/geo-sharding/" + this.props.curClusterId + "/model")
            .then(
                res => {
                    this.setState({
                        models : res.data,
                        isLoadedModels : true
                    })
                })
            .catch(
                error => {
                    this.setState({
                        isLoadedModels : true,
                        error
                    })
                    openNotificationWithIcon("error", error.message, "Can't update the list of types.")
                }
            )
    }

    showDefaultFields() {
        const defaultFields = (
            <div>
                <Form.Item
                    label="Object name"
                    name="name"
                    rules={[
                        {
                            required: true,
                            message: 'Please input object\'s name',
                        },
                    ]}
                >
                    <Input placeHolder="Ex. Camera 1" name="name" onChange={this.onChangeInput}/>
                </Form.Item>
                <Form.Item
                    label="Description"
                    name="description"
                    rules={[
                        {
                            required: false,
                        },
                    ]}
                >
                    <TextArea placeHolder="Some notes..." rows={4} name="description" onChange={this.onChangeInput}/>
                </Form.Item>
                <Form.Item
                    label="Geo-pos-x"
                    name="geo-pos-x"
                    rules={[
                        {
                            required: true,
                            message: "Enter geo-pos-x"
                        },
                    ]}
                >
                    <Input style={{ width: '20%' }} name="geo-pos-x" onChange={this.onChangeInput}/>
                </Form.Item>
                <Form.Item
                    label="Geo-pos-y"
                    name="geo-pos-y"
                    rules={[
                        {
                            required: true,
                            message: "Enter geo-pos-y"
                        },
                    ]}
                >
                    <Input style={{ width: '20%' }} name="geo-pos-y" onChange={this.onChangeInput}/>
                </Form.Item>
            </div>
        )
        if (this.state.typeOfNewObject != null) {
            return defaultFields;
        }
    }
}