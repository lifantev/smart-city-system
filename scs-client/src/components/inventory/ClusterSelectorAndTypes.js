import React, {Component} from 'react'
import {Select} from 'antd'
import "antd/dist/antd.css"
import "./css/ClusterSelectorAndTypes.css"
import ClusterModelsMenuComponent from "./ClusterTypesMenuComponent.js"
import {GetAxios} from "../GetAxios";

const { Option } = Select;

export default class ClusterSelectorAndTypes extends Component {

    /**
     * Конструктор класса. Здесь мы определяем начальное состояние и биндим необходимые методы
     * @param props - проперти, которые прилетают к нам из родительского компонента
     */
    constructor(props) {
        super(props);
        this.state = {
            clusters : [],
            isLoadedClusters : false,
            isLoadedModels : false,
            models : [],
            error : null,
            curClusterId : "-1",
        }
    }

    /**
     * этот метод обновляет список кластеров.
     */
    updateClusterList() {
        this.setState({
            isLoadedClusters : false
        })
        GetAxios().get('/api/v1/geo-sharding/config')
            .then(
                res => {
                    if (res.status === 200) {
                        this.setState({
                            clusters : res.data,
                            isLoadedClusters : true
                        });
                    }
                })
            .catch(
                error => {
                    //ошибка запроса. Например, потерянное соединение, без ответа от сервера.
                    if (error.request) {
                        this.setState({
                            isLoadedClusters : true,
                            error
                        })
                    }
                    //ошибки типа 4хх и 5хх
                    else if (error.response) {
                        this.setState({
                            isLadedClusters : true,
                            error
                        })
                    }
                    //прочие ошибки
                    else {
                        this.setState({
                            isLadedClusters : true,
                            error
                        })
                    }
                })
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
                        isLoadedModels : true
                    })
                })
            .catch(
                error => {
                    this.setState({
                        isLoadedModels : true,
                        error
                    })
                }
            )
    }


    /**
     * Метод, который относится к жизненному циклу компонента. Он вызывается при каждом первом запуске кмопонента.
     */
    componentDidMount() {
        this.updateClusterList()
    }

    /**
     * Главный метод, который возвращает вид компонента
     * @returns {JSX.Element} - html код нашего компонента
     */
    render() {
        if (this.state.error != null) {
            return (
                <div className="ErrorText">Somme problems... {this.state.error.message}</div>
            )
        }
        else if (!this.state.isLoadedClusters) {
            return (
                <div style={{width:400}}>Loading...</div>
            )
        }
        else {
            return (
                <div>
                    <div>
                        <div className="HeaderText">
                            Выберете Кластер
                        </div>
                        <Select className="Selector" defaultValue="None" style={{ width: "50%" }} onChange={this.handleChange}>
                            <Option key="-1"> None </Option>
                            {this.state.clusters.map((item) => (
                                <Option key={item['shard-id']}> {item.name} </Option>
                            ))}
                        </Select>
                    </div>
                    <ClusterModelsMenuComponent
                        curClusterId={this.state.curClusterId}
                        isLoadedModels={this.state.isLoadedModels}
                        error={this.state.error}
                        models={this.state.models}
                        setCurTypeMethod={this.props.setCurTypeMethod}
                    />
                </div>
            )
        }
    }

    /**
     * Метод вызывается, когда мы выбираем отличный от предыдущего элемент списка кластеров
     * @param shardId - новый элемент списка кластеров
     */
    handleChange = (shardId) => {
        //меняем url
        window.history.pushState(null, null,  "/inventory/" + shardId);
        this.setState({          //изменяем состояния, даем понять, что мы загрузились
            curClusterId : shardId,
            isLoadedModels : false,
        })
        this.props.setClusterIdMethod(shardId);   //оповещаем наш родительский компонент о том, что мы изменили текущий кластер
        this.props.setCurTypeMethod(null);   //если ничего не выбрали, то убираем список
        if (shardId !== "-1") {
            this.updateModelsOfCluster(shardId);   //перезагружаем список кластеров
        }
    }




}