package homework;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class SlideWindowCompressionMain {
	public static void main(String[] args)throws Exception{
		          //-----------------------1�����ArrayList�����File����������Ͷ���-------------------------------------------------//
		          ArrayList<ENPoint> pGPSArrayInit = new ArrayList<ENPoint>();//ԭ��¼��γ����������
		          ArrayList<ENPoint> pGPSArrayFilter = new ArrayList<ENPoint>();//���˺�ľ�γ����������
		          ArrayList<ENPoint> pGPSArrayFilterSort = new ArrayList<ENPoint>();//���˲������ľ�γ����������
		          File fGPS = new File("2007-10-14-GPS.log");//ԭʼ�����ļ�����
		          File oGPS = new File("2017-10-12-GPS-Result.log");//���˺�Ľ�������ļ�����
		          //����ת���ɶȺ��ԭʼ��γ�������ļ������ָ�ʽΪ��ID#��γֵ��γ��ֵ�������о��Ⱥ�ά�ȵ�λΪ�ȣ�������С�����6λ����
		          File fInitGPSPoint = new File("2007-10-14-GPS-ENPoint.log");//����ת�����ԭʼ��γ�������������ļ�
		          File fTestInitPoint = new File("2007-10-14-GPS-InitTestPoint.log");//���ڷ����ԭʼ��γ������������ļ�
		          File fTestFilterPoint = new File("2017-10-12-GPS-FilterTestPoint.log");//���ڷ���Ĺ��˺�ľ�γ������������ļ�
		          //-------------------------2����ȡԭʼ�����겢����д�뵽�ļ���-------------------------------------------------------//
		          pGPSArrayInit = getENPointFromFile(fGPS);//��ԭʼ�����ļ��л�ȡת����ľ�γ����������ݣ���ŵ�ArrayList������
		          writeInitPointToFile(fInitGPSPoint, pGPSArrayInit);//��ת�����ԭʼ��γ�ȵ�����д���ļ���
		          System.out.println(pGPSArrayInit.size());//���ԭʼ��γ�ȵ�����ĸ���
		          //-------------------------3�����й켣ѹ��-----------------------------------------------------------------------//
		          double DMax = 30.0;//�趨�����������ֵ
		          pGPSArrayFilter.add(pGPSArrayInit.get(0));//��ȡ��һ��ԭʼ��γ�ȵ����겢��ӵ����˺��������
		          pGPSArrayFilter.add(pGPSArrayInit.get(pGPSArrayInit.size()-1));//��ȡ���һ��ԭʼ��γ�ȵ����겢��ӵ����˺��������
		          ENPoint[] enpInit = new ENPoint[pGPSArrayInit.size()];//ʹ��һ��������������еĵ����꣬���ں����ѹ��
		          Iterator<ENPoint> iInit = pGPSArrayInit.iterator();
		          int jj=0;
		          while(iInit.hasNext()){
		              enpInit[jj] = iInit.next();
		              jj++;
		          }//��ArrayList�еĵ����꿽������������
		          int start = 0;//��ʼ�±�
		          
		          //----------�޸�-----//
		          int end = 2;//pGPSArrayInit.size()-1;//�����±�
		          int cur=1;
		          int m=1;
		          int count=pGPSArrayInit.size()-1;
		          SWCompress(enpInit,pGPSArrayFilter,start,end,cur,m,DMax,count);//DPѹ���㷨
		          
		          
		          System.out.println(pGPSArrayFilter.size());//���ѹ����ĵ���
		          //-------------------------4����ѹ����ľ�γ�ȵ��������ݰ���ID��С��������---------------------------------------------//
		          ENPoint[] enpFilter = new ENPoint[pGPSArrayFilter.size()];//ʹ��һ����������չ��˺�ĵ����꣬���ں��������
		          Iterator<ENPoint> iF = pGPSArrayFilter.iterator();
		          int i = 0;
		          while(iF.hasNext()){
		              enpFilter[i] = iF.next();
		              i++;
		          }//��ArrayList�еĵ����꿽������������
		          Arrays.sort(enpFilter);//��������
		          for(int j=0;j<enpFilter.length;j++){
		              pGPSArrayFilterSort.add(enpFilter[j]);//�������ĵ�����д��һ���µ�ArrayList������
		          }
		          //-------------------------5�����ɷ�������ļ�--------------------------------------------------------------------//
		          writeTestPointToFile(fTestInitPoint,pGPSArrayInit);//��ԭʼ��γ�����ݵ�д������ļ��У���ʽΪ�����ȣ�ά�ȡ�
		          writeTestPointToFile(fTestFilterPoint, pGPSArrayFilterSort);//�����˺�ľ�γ�����ݵ�д������ļ��У���ʽΪ�����ȣ�ά�ȡ�
		          //-------------------------6����ƽ�����-------------------------------------------------------------------------//
		          double mDError = getMeanDistError(pGPSArrayInit,pGPSArrayFilterSort);//��ƽ�����
		          System.out.println(mDError);
		          //-------------------------7����ѹ����--------------------------------------------------------------------------//
		          double cRate = (double)pGPSArrayFilter.size()/pGPSArrayInit.size()*100;//��ѹ����
		          System.out.println(cRate);
		          //-------------------------8���������ս���ļ�--------------------------------------------------------------------//
		          //�����ս��д�����ļ��У��������˺�ĵ��ID����ĸ�����ƽ������ѹ����
		          writeFilterPointToFile(oGPS,pGPSArrayFilterSort,mDError,cRate);
		          //------------------------------------------------------------------------------------------------------------//
		      }
		  
		      /**
		       *�������ܣ���Դ�ļ��ж������Լ�¼�еľ�γ�����꣬�����뵽ArrayList�����У������䷵��
		       * @param fGPS��Դ�����ļ�
		       * @return pGPSArrayInit�����ر������е������ArrayList����
		       * @throws Exception
		       */
		      public static ArrayList<ENPoint> getENPointFromFile(File fGPS)throws Exception{
		          ArrayList<ENPoint> pGPSArray = new ArrayList<ENPoint>();
		          if(fGPS.exists()&&fGPS.isFile()){
		              InputStreamReader read = new InputStreamReader(new FileInputStream(fGPS));
		              BufferedReader bReader = new BufferedReader(read);
		              String str;
		              String[] strGPS;
		              int i = 0;
		              while((str = bReader.readLine())!=null){
		                  strGPS = str.split(" ");
		                  ENPoint p = new ENPoint();
		                  p.id = i;
		                  i++;
		                  p.pe = (dfTodu(strGPS[3]));
		                  p.pn = (dfTodu(strGPS[5]));
		                  pGPSArray.add(p);
		              }
		              bReader.close();
		          }
		          return pGPSArray;
		      }
		  
		      /**
		       * �������ܣ������˺�ĵ�ľ�γ�����ꡢƽ��������ѹ����д������ļ���
		       * @param outGPSFile������ļ�
		      * @param pGPSPointFilter�����˺�ĵ�
		      * @param mDerror��ƽ���������
		      * @param cRate��ѹ����
		      * @throws Exception
		      */
		     public static void writeFilterPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter,
		                                               double mDerror,double cRate)throws Exception{
		         Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
		         RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
		         while(iFilter.hasNext()){
		             ENPoint p = iFilter.next();
		             String sFilter = p.getResultString()+"\n";
		             byte[] bFilter = sFilter.getBytes();
		             rFilter.write(bFilter);
		         }
		         String strmc = "#"+Integer.toString(pGPSPointFilter.size())+","+
		                 Double.toString(mDerror)+","+Double.toString(cRate)+"%"+"#"+"\n";
		         byte[] bmc = strmc.getBytes();
		         rFilter.write(bmc);
		 
		         rFilter.close();
		     }
		     /**
		      * �������ܣ���ת�����ԭʼ��γ�����ݵ�浽�ļ���
		      * @param outGPSFile
		      * @param pGPSPointFilter
		      * @throws Exception
		      */
		     public static void writeInitPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter)throws Exception{
		         Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
		         RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
		         while(iFilter.hasNext()){
		             ENPoint p = iFilter.next();
		             String sFilter = p.toString()+"\n";
		             byte[] bFilter = sFilter.getBytes();
		             rFilter.write(bFilter);
		         }
		         rFilter.close();
		     }
		     /**
		      * �������ܣ��������еľ�γ�ȵ���������д������ļ��У����ڿ��ӻ�����
		      * @param outGPSFile���ļ�����
		      * @param pGPSPointFilter��������
		      * @throws Exception
		      */
		     public static void writeTestPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter)throws Exception{
		         Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
		         RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
		         while(iFilter.hasNext()){
		             ENPoint p = iFilter.next();
		             String sFilter = p.getTestString()+"\n";
		             byte[] bFilter = sFilter.getBytes();
		             rFilter.write(bFilter);
		         }
		         rFilter.close();
		     }
		 
		     /**
		      * �������ܣ���ԭʼ��γ����������ת���ɶ�
		      * @param str��ԭʼ��γ������
		      * @return �����ض��ڵĶ�����
		      */
		     public static double dfTodu(String str){
		         int indexD = str.indexOf('.');
		         String strM = str.substring(0,indexD-2);
		         String strN = str.substring(indexD-2);
		         double d = Double.parseDouble(strM)+Double.parseDouble(strN)/60;
		         return d;
		     }
		     /**
		      * �������ܣ�����һ��double����С�������λ
		      * @param d��ԭʼdouble��
		      * @return ����ת�����double��
		      */
		     public static double getPointSix(double d){
		         DecimalFormat df = new DecimalFormat("0.000000");
		         return Double.parseDouble(df.format(d));
		     }
	       /**
		      * �������ܣ�ʹ�������������ʹ�ú��׹�ʽ��ã���ȷ��������pX����pA��pB��ȷ����ֱ�ߵľ���
		      * @param pA����ʼ��
		      * @param pB��������
		      * @param pX����������
		      * @return distance����pX��pA��pB����ֱ�ߵľ���
		      */
		     public static double distToSegment(ENPoint pA,ENPoint pB,ENPoint pX){
		         double a = Math.abs(geoDist(pA, pB));
		         double b = Math.abs(geoDist(pA, pX));
		         double c = Math.abs(geoDist(pB, pX));
		         double p = (a+b+c)/2.0;
		         double s = Math.sqrt(Math.abs(p*(p-a)*(p-b)*(p-c)));
		         double d = s*2.0/a;
		         return d;
		     }
		 
		     /**
		      * �������ܣ�����ʦ���Ŀ������ķ�����������γ�ȵ�֮��ľ���
		      * @param pA����ʼ��
		      * @param pB��������
		      * @return distance������
		      */
		     public static double geoDist(ENPoint pA,ENPoint pB)
		     {
		         double radLat1 = Rad(pA.pn);
		         double radLat2 = Rad(pB.pn);
		         double delta_lon = Rad(pB.pe - pA.pe);
		         double top_1 = Math.cos(radLat2) * Math.sin(delta_lon);
		         double top_2 = Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
		         double top = Math.sqrt(top_1 * top_1 + top_2 * top_2);
		         double bottom = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
		         double delta_sigma = Math.atan2(top, bottom);
		         double distance = delta_sigma * 6378137.0;
		         return distance;
		     }
		     /**
		      * �������ܣ��Ƕ�ת����
		      * @param d���Ƕ�
		      * @return ���ص��ǻ���
		      */
		     public static double Rad(double d)
		     {
		         return d * Math.PI / 180.0;
		     }
		     
		     /**
		      * �������ܣ��������������ƣ�����DP�����ݹ�Ķ�ԭʼ�켣���в������õ�ѹ����Ĺ켣
		      * @param enpInit��ԭʼ��γ�����������
		      * @param enpArrayFilter�����ֹ��˺�ĵ���������
		      * @param start����ʼ�±�
		      * @param end���յ��±�
		      * @param DMax��Ԥ��ָ���õ����������
		      */
		     public static void SWCompress(ENPoint[] enpInit,ArrayList<ENPoint> enpArrayFilter,
		                                      int start,int end,int cur,int m,double DMax,int count){
		         if(end < count){//�ݹ���е�����start<end
		        	    double curDist = distToSegment(enpInit[start], enpInit[end], enpInit[cur]);  //��ǰ�㵽��Ӧ�߶εľ���
		        	    double maxDist = distToSegment(enpInit[start], enpInit[end], enpInit[m]); //��ǰ�㵽��Ӧ�߶εľ���
		        	        if (curDist > DMax | maxDist > DMax) {
		        	        	enpArrayFilter.add(enpInit[cur]);//����ǰ����뵽����������
		        	            //enpArrayFilter.append(enpInit[cur])  # ����ǰ����뵽����������
		        	            start = cur;
		        	            cur = start + 1;
		        	            end = start + 2;
		        	            m = cur;
		        	            maxDist = 0;
		        	            SWCompress(enpInit, enpArrayFilter, start, end, cur, m, DMax, count);
		        	        }
		        	        else if((curDist<=DMax)&(maxDist<=DMax)){
		        	            if (curDist > maxDist)
		        	                m = cur;
		        	            cur = end;
		        	            end = end + 1;
		        	            SWCompress(enpInit, enpArrayFilter, start, end, cur, m, DMax, count);
		        	        }
		        	
		         }
		     }
		     /**
		      * �������ܣ���ƽ���������
		      * @param pGPSArrayInit��ԭʼ���ݵ�����
		      * @param pGPSArrayFilterSort�����˺�����ݵ�����
		      * @return ������ƽ������
		      */
		     public static double getMeanDistError(
		             ArrayList<ENPoint> pGPSArrayInit,ArrayList<ENPoint> pGPSArrayFilterSort){
		         double sumDist = 0.0;
		         for(int i=1;i<pGPSArrayFilterSort.size();i++){
		             int start = pGPSArrayFilterSort.get(i-1).id;
		             int end = pGPSArrayFilterSort.get(i).id;
		             for(int j=start+1;j<end;j++){
		                 sumDist += distToSegment(
		                         pGPSArrayInit.get(start),pGPSArrayInit.get(end),pGPSArrayInit.get(j));
		             }
		         }
		         double meanDist = sumDist/(pGPSArrayInit.size());
		         return meanDist;
		     }
		 }

