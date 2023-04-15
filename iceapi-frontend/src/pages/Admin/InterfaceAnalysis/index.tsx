import {PageContainer,} from '@ant-design/pro-components';
import '@umijs/max';
import React, {useEffect, useState} from 'react';
import ReactEcharts from 'echarts-for-react';
import {listTopInterfaceInvokeUsingGET} from "@/services/iceapi-backend/analysisController";

/**
 * 接口分析
 * @constructor
 */
const InterfaceAnalysis: React.FC = () => {
  const [data, setData] = useState<API.InterfaceInfoVO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    //todo 从远程获取数据
    try {
      listTopInterfaceInvokeUsingGET().then(res => {
        if(res.data) {
          setData(res.data);
        }
      })
    } catch (e: any) {

    }
  },[])

  const chartData = data.map(item => {
    return {
      value: item.totalNum,
      name: item.name
    }
  })

  const options = {
    title: {
      text: '最多调用次数的接口TOP3',
      subtext: 'Real Data',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '接口名称',
        type: 'pie',
        radius: '50%',
        data: chartData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };


  return (
    <PageContainer>
      <ReactEcharts loadingOption={
        {showLoading: loading}
      } option={options}/>
    </PageContainer>
  );
};
export default InterfaceAnalysis;
