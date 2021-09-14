import React, {Component} from 'react'
import {List, Divider, Table, Space, Button, Row, Col, Input, Modal, notification, Popconfirm, message, Collapse} from 'antd';
import {DownOutlined, SearchOutlined, SettingOutlined} from '@ant-design/icons';
import "./css/ClusterObjectManagement.css"
import Highlighter from 'react-highlight-words';
import AddNewObjectComponent from "./AddNewObjectComponent";
import {GetAxios} from "../GetAxios";

const { Panel } = Collapse;
const {TextArea} = Input;
const expandable = { expandedRowRender: record => <p>{record.description}</p> };
const openNotificationWithIcon = (type, message, description) => {
    notification[type]({
        message: message,
        description:
        description,
    });
};

export default class ClusterObjectManagement extends Component {

    /**
     * Соглашение
     * this.state[*parameter_name*EditValue] - значение в текущем редактируемом поле.
     * this.state[*parameter_name*Edit]      - id объекта, поле которого мы сейчас редактируем
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            isLoadedData : false,
            virginData : null,
            dataWithKey : null,
            error : null,
            searchText: '',
            searchedColumn: '',
            visibleModal: false,
            loadingModal : false,
            models : null,
            isLoadingParameters : false
        }
    }

    /**
     * Обновляет список типов для нового кластера
     */
    updateModelsOfCluster(shardId) {
        GetAxios().get("/api/v1/geo-sharding/" + shardId + "/model")
            .then(
                res => {
                    this.setState({
                        models : res.data,
                    })
                })
            .catch(
                error => {
                    this.setState({
                        error
                    })
                }
            )
    }

    updateVirginData() {
        GetAxios().get("/api/v1/geo-sharding/" + this.props.clusterId + "/objects")
            .then(
                res => {
                    const data = [];
                    res.data.map(item => {
                        if (item.type === this.props.curType) {
                            data.push({
                                key : item.id,
                                name : item.name,
                                id : item.id,
                                description : item.description,
                                type : item.type,
                                geo_pos_x : item['geo-pos-x'],
                                geo_pos_y : item['geo-pos-y'],
                                parameters : item['parameters']
                            })
                        }
                    });
                    this.setState({
                        dataWithKey: data,
                        isLoadedData: true,
                        virginData : res.data
                    });
                })
            .catch(
                error => {
                    alert("Error: " + error.message)
                    this.setState({
                        isLoadedData : true,
                        error
                    })
                }
            )
    }

    /**
    * Обновляет данные списка: либо загружает с сервера, либо просто меняет
    * */
    updateData() {
        if (this.props.clusterId === "-1") {
            return ;
        }
        this.updateModelsOfCluster(this.props.clusterId);
        if (this.state.virginData != null) {
            const newData = [];
            this.state.virginData.map(item => {
                if (item.type === this.props.curType) {
                    newData.push({
                        key : item.id,
                        name : item.name,
                        id : item.id,
                        description : item.description,
                        type : item.type,
                        geo_pos_x : item['geo-pos-x'],
                        geo_pos_y : item['geo-pos-y'],
                        parameters : item['parameters']
                    })
                }
            })
            this.setState({
                dataWithKey : newData,
                isLoadedData: true,
            })
            return ;
        }
        else {
            this.updateVirginData();
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        // если в компоненту передали такой другой clusterId, в отличае от предыдущего состояния (предыдущего раза,
        // когда вызывали рендеринг этого компонента), то есть когда был выбран из списка другой кластер, тогда  мы заменяем
        // существующую информацию на новую
        if (prevProps.clusterId !== this.props.clusterId) {
            this.setState({
                isLoadedData : false,
                virginData : null,
            })
            this.updateData();
            return ;
        }

        //а тут если был выбран другой тип, отрисовываем новый список с объектами другого типа.
        if (prevProps.curType !== this.props.curType) {
            this.setState({
                isLoadedData : false
            })
            this.updateData();
            return ;
        }
    }

    showModal = () => {
        this.setState({
            visibleModal: true,
        });
    };

    setLoadingModal = (loading) => {
        this.setState({
            loadingModal : loading
        })
    }

    setVisibleModal = (visible) => {
        this.setState({
            visibleModal : visible
        })
    }

    deleteItem = (record) => {
        GetAxios().delete("/api/v1/data/" + record.id)
            .then(
                res => {
                    if (res.status === 200) {
                        openNotificationWithIcon("info", "Deleted!" ,"Object has been deleted!");
                        this.updateVirginData();
                    }
                    else {
                        openNotificationWithIcon("error", "Error while deleting!",
                            "Can't delete because of server's mistake. Status: " + res.status)
                    }
                })
            .catch(
                error => {
                    openNotificationWithIcon("error", "Error while deleting!", error.message)
                    this.setState({
                        error
                    })

                }
            )
    }

    sortMethod = (a, b) => {
        let i = 0;
        while (i < a.length && i < b.length) {
            if (a.codePointAt(i) !== b.codePointAt(i)) {
                return a.codePointAt(i) - b.codePointAt(i);
            }
            i++;
        }
        return 0;
    }

    render() {
        const columns = [
            {
                title: 'Id',
                dataIndex: 'id',
                sorter: (a, b) => this.sortMethod(a.id, b.id),
                sortDirections: ['descend', 'ascend'],
                ...this.getColumnSearchProps('id')
            },
            {
                title: 'Name',
                dataIndex: 'name',
                sorter: (a, b) => this.sortMethod(a.name, b.name),
                sortDirections: ['descend', 'ascend'],
                ...this.getColumnSearchProps('name')
            },
            {
                title: 'Type',
                dataIndex: 'type',
            },
            {
                title: 'GeoPosX',
                dataIndex: 'geo_pos_x',
            },
            {
                title: 'GeoPosY',
                dataIndex: 'geo_pos_y',
            },
            {
                title: 'Action',
                key: 'action',
                width:"30%",
                render: (record) => (
                    <Space size="middle">
                        <Popconfirm
                            title="Are you sure to delete this object?"
                            onConfirm={(e) => {
                                this.deleteItem(record);
                            }}
                            onCancel={() => {
                                message.info("Canceled");
                            }}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Button type="link">Delete</Button>
                        </Popconfirm>
                        <Button type="link">Edit</Button>
                        <a className="ant-dropdown-link">
                            Actions <DownOutlined />
                        </a>
                    </Space>
                ),
            },
        ];
        if (this.props.curType === null) {
            return (<div> </div>)
        }
        else if(this.props.curType != null){
            return (
                <div className="Table">
                    <Col>
                        <Row style={{marginTop: "20px", marginBottom : "10px"}}>
                            <Button onClick={this.showModal}>Add new Object</Button>
                            <AddNewObjectComponent
                                updateVirginData={this.updateVirginData.bind(this)}
                                visible={this.state.visibleModal}
                                loading={this.state.loadingModal}
                                setLoadingModal={this.setLoadingModal}
                                setVisibleModal={this.setVisibleModal}
                                curClusterId={this.props.clusterId}
                            />
                        </Row>
                        <Row>
                            <Table columns={columns}
                                   dataSource={this.state.dataWithKey}
                                   loading={!this.state.isLoadedData}
                                   expandable={{
                                       expandedRowRender: record => <div>{this.expandedData(record)}</div>,
                                       rowExpandable: record => record.name !== 'Not Expandable',
                                   }}
                                   tableLayout="fixed"
                            />
                        </Row>
                    </Col>

                </div>
            )
        }
    }

    expandedData = (record) => {
        let params = [];
        this.state.models.map(model => {
            if (model.type === record.type) {
                model.attributes.map(attr => {
                    let kek = {};
                    kek['id'] = record.id;
                    kek['displayed-name'] = attr['displayed-name'];
                    kek['name'] = attr['name'];
                    kek['value'] = record.parameters[attr.name];
                    params.push(kek);
                })
            }
        })
        return (
            <div>
                <Divider orientation="left">
                    Description: {(this.state['descriptionEdit'] != null) ?
                    <div></div> :
                    <Button type="link" onClick={this.onClickEditDescription.bind(this, record)}>Edit</Button>}
                </Divider>
                {this.showDescriptionField(record)}
                <Divider orientation="left">Parameters</Divider>
                <List
                    dataSource={params}
                    itemLayout="horizontal"
                    loading={this.state.isLoadingParameters}
                    renderItem={item => this.showEditField(record, item)}
                />
            </div>
        )
    }

    /**
     * меняет элемент списка параметров объекта для редактирования
     * @param item
     * @param record
     */
    editParameter(item, record) {
        let state = {};
        state[item.name + "Edit"] = record.id;
        state[item.name + "EditValue"] = item.value;
        this.setState(state);
    }

    /**
     * возвращает элемент списка параметров объекта. Либо поле для редактирования, либо просто сам параметр
     * @param record
     * @param item
     * @returns {JSX.Element}
     */
    showEditField = (record, item) => {
        if (this.state[item.name + "Edit"] !== record.id) {
            return (
                <List.Item
                    actions={[<Button  onClick={this.editParameter.bind(this, item, record)}>edit</Button>]}
                >
                    <div>{item['displayed-name']} : {item.value}</div>
                </List.Item>
            )
        }
        else {
            return (
                <List.Item
                    actions={[
                        <Popconfirm
                            title="Save changes?"
                            onConfirm={this.onClickSaveChanges.bind(this, record, item.name)}
                            okText="Yes"
                            cancelText="No">
                            <Button >Save</Button>
                        </Popconfirm>,
                        <Popconfirm
                            title="Discard changes?"
                            onConfirm={this.onClickCancelEditParameter.bind(this, item.name)}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Button>Cancel</Button>
                        </Popconfirm>

                    ]}
                >
                    <Row>
                        <Col>{item['displayed-name']} : </Col>
                        <Col>
                            <Input value={this.state[item.name + "EditValue"]} name={item.name} onChange={this.changeInput.bind(this)}/>
                        </Col>
                    </Row>
                </List.Item>
            )
        }
    }

    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => {
                        setSelectedKeys(e.target.value ? [e.target.value] : []);
                        this.setState({
                            searchText: e.target.value,
                            searchedColumn: dataIndex,
                        });
                        confirm({ closeDropdown: false }); //подтверждаем поиск элементов. Окно не закрывается при этом
                    }}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ marginBottom: 8, display: 'block' }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Search
                    </Button>
                    <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
                        Reset
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: filtered => <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />,
        onFilter: (value, record) => {
            return record[dataIndex]
                ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
                : ''
        },
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => this.searchInput.select(), 100);
            }
        },
        render: text => {
            return this.state.searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                text
            )
        },
    });

    handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex,
        });
    };

    handleReset = clearFilters => {
        clearFilters();
        this.setState({ searchText: '' });
    };

    showDescriptionField = (record) => {
        if (this.state.descriptionEdit === record.id) {
            return (
                <div>
                    <TextArea
                        rows={4}
                        name="description"
                        value={this.state.descriptionEditValue}
                        onChange={this.changeInput.bind(this)}/>
                    <Space size="middle" style={{marginTop:10}}>
                        <Popconfirm
                            title="Save changes?"
                            onConfirm={this.onClickSaveChanges.bind(this, record, "description")}
                            okText="Yes"
                            cancelText="No">
                            <Button name={record.name} id={record.id}>Save</Button>
                        </Popconfirm>
                        <Popconfirm
                            title="Discard changes?"
                            onConfirm={this.onClickCancelEditParameter.bind(this, "description")}
                            okText="Yes"
                            cancelText="No">
                            <Button name={record.name} id={record.id}>Cancel</Button>
                        </Popconfirm>
                    </Space>
                </div>
            )
        }
        if (record.description) {
            return <div>
                <div>{record.description}</div>
            </div>
        }
        else return <div>"No description."</div>
    }

    onClickEditDescription = (item) => {
        this.setState({
            descriptionEdit: item.id,
            descriptionEditValue : item.description
        })
    }

    changeInput(event) {
        let state = {};
        state[event.target.name + 'EditValue'] = event.target.value;
        this.setState(state);
    }

    onClickCancelEditParameter(fieldName) {
        let state = {};
        state[fieldName + "Edit"] = null;
        state[fieldName + "EditValue"] = null;

        this.setState(state);
    }

    /**
     *
     * @param record    - объект, чей параметр мы меняем
     * @param fieldName - название параметра, который мы сохраняем
     */
    onClickSaveChanges(record, fieldName) {
        this.setState({
            isLoadingParameters :true
        })
        let state = {};
        state[fieldName] = this.state[fieldName + "EditValue"];
        if (fieldName !== "description") {
            let stateParams = state;
            state = {};
            state['parameters'] = stateParams;
        }
        GetAxios().patch(
            "/api/v1/data/" + record.id,
            JSON.stringify(state),
            {headers : {"Content-Type" : "application/json"}}
        )
            .then(
                res => {
                    if (res.status === 200) {
                        message.info("Success!");
                        this.onClickCancelEditParameter(fieldName);
                        this.updateVirginData();
                    }
                    else {
                        message.error("Can't update data!");
                    }
                    this.setState({
                        isLoadingParameters: false
                    });
                })
            .catch(
                error => {
                    message.error(error.message);
                    this.setState({
                        isLoadingParameters: false
                    });
                }
            )
    }
}