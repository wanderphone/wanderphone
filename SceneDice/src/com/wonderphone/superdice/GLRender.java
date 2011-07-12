package com.wonderphone.superdice;

import java.nio.ByteBuffer;

import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.wonderphone.superdice.Start.IndexThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.ImageView;
//openGL构建模型
public class GLRender implements Renderer,SensorEventListener


{
	
	 private HashMap<Integer, Integer> soundPoolMap;    
	boolean rockflag;
	int one = 0x10000;
	float step = 10.0f;
	boolean key;
	boolean light = true;
	int flag;
	int abc=0;
	int activityflag;
	Context context;

	private IntBuffer   mVertexBuffer;
	private IntBuffer   mVertexBuffer1;
	private IntBuffer   mTexCoords;
	private IntBuffer   mTexCoords1;
	private IntBuffer   mNormals;
	private IntBuffer   mNormals1;
	private ByteBuffer  mIndices1;
	private ByteBuffer  mIndices2;
	private ByteBuffer  mIndices3;
	private ByteBuffer  mIndices4;
	private ByteBuffer  mIndices5;
	private ByteBuffer  mIndices6;
	private ByteBuffer  mIndices7;
	
	//���ҡ����ر��� 
	 
	private long lastTime = 0; 
	 
	private long curTime = 0; 
	 
	private long duration = 0; 
	 
	 
	 
	private float last_x = 0.0f; 
	 
	private float last_y = 0.0f; 
	 
	private float last_z = 0.0f; 
	 
	 
	 
	private float shake = 0.0f; 
	 
	private float totalShake = 0.0f; 
	 
	
	float xrot=45+13, yrot=45,zrot=0 ,flagrot;	//x,y����ת
	float xspeed, yspeed,zspeed;//��ת���ٶ�
	Bitmap[] bm = new Bitmap[6];
	
	//���廷����(r,g,b,a)
	//FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.5f,0.5f,0.5f,1.0f}); 
	//���������
	//FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});
	//��Դ��λ��
	//FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{0.0f,0.0f,2.0f,1.0f}); 
	
	//���˵�����
	int filter = 1;
	//����Ч��
	int [] texture;
	
	int vertices[] = {
			-one,-one,one,
			one,-one,one,
			one,one,one,
			-one,one,one,
			
			-one,-one,-one,
			-one,one,-one,
			one,one,-one,
			one,-one,-one,
			
			-one,one,-one,
			-one,one,one,
			one,one,one,
			one,one,-one,
			
			-one,-one,-one,
			one,-one,-one,
			one,-one,one,
			-one,-one,one,
			
			one,-one,-one,
			one,one,-one,
			one,one,one,
			one,-one,one,
			
			-one,-one,-one,
			-one,-one,one,
			-one,one,one,
			-one,one,-one,
			
	};
	//����ͼƬ
	int texCoords1[] ={
			0,one,
			one,one,
			one,0,
			0,0,
	};
	int vertices1[] = {
			-10*one,-15*one,one,
			10*one,-15*one,one,
			10*one,15*one,one,
			-10*one,15*one,one,
			
			
			
	};
	int normals1[] = {
			0,0,one,
			0,0,one,
			0,0,one,
			0,0,one,
			
		
	};

	
	int normals[] = {
			0,0,one,
			0,0,one,
			0,0,one,
			0,0,one,
			
			0,0,one,
			0,0,one,
			0,0,one,
			0,0,one,
			
			0,one,0,
			0,one,0,
			0,one,0,
			0,one,0,
			
			0,-one,0,
			0,-one,0,
			0,-one,0,
			0,-one,0,
			
			one,0,0,
			one,0,0,
			one,0,0,
			one,0,0,
			
			-one,0,0,
			-one,0,0,
			-one,0,0,
			-one,0,0,
	};
	
	int texCoords[] = {
		one,one,
		one,0,
		0,0,
		0,one,

		0,0,
		0,one,
		one,one,
		one,0,
		
		0,one,
		one,one,
		one,0,
		0,0,
		
		0,one,	
		one,one,
		one,0,
		0,0,	
		
		
		0,0,
		0,one,
		one,one,
		one,0,
		
		
		one,one,
		one,0,
		0,0,
		0,one,
	};
	


	
	/*ByteBuffer indices = ByteBuffer.wrap(new byte[]{
			0,1,3,2,
			4,5,7,6,
			8,9,11,10,
			12,13,15,14,
			16,17,19,18,
			20,21,23,22,
	});*/
	byte indices1[] = {
			0,1,3,2,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	};
	byte indices2[] = {
			0,0,0,0,
			4,5,7,6,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	};
	byte indices3[] = {
			0,0,0,0,
			0,0,0,0,
			8,9,11,10,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	};
	byte indices4[] = {
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			12,13,15,14,
			0,0,0,0,
			0,0,0,0,
	};
	byte indices5[] = {
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			16,17,19,18,
			0,0,0,0,
	};
	byte indices6[] = {
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			20,21,23,22,
	};
	//����
	byte indices7[] = {
			0,1,3,2,
			4,5,7,6,
			8,9,11,10,
			12,13,15,14,
			
			
	};

	public GLRender(int tmpflag)
	{
		activityflag=tmpflag;
		//final IndexThread thread = new IndexThread();
       
     //  thread.start();
		
	
	}

	
    
	@Override
	public void onDrawFrame(GL10 gl)
	{

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        ByteBuffer vbb1 = ByteBuffer.allocateDirect(vertices1.length*4);
        vbb1.order(ByteOrder.nativeOrder());
        mVertexBuffer1 = vbb1.asIntBuffer();
        mVertexBuffer1.put(vertices1);
        mVertexBuffer1.position(0);
        
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(normals.length*4);
        vbb2.order(ByteOrder.nativeOrder());
        mNormals = vbb2.asIntBuffer();
        mNormals.put(normals);
        mNormals.position(0);
        
        ByteBuffer vbb12 = ByteBuffer.allocateDirect(normals1.length*4);
        vbb12.order(ByteOrder.nativeOrder());
        mNormals1 = vbb12.asIntBuffer();
        mNormals1.put(normals1);
        mNormals1.position(0);
        
        ByteBuffer vbb3 = ByteBuffer.allocateDirect(texCoords.length*4);
        vbb3.order(ByteOrder.nativeOrder());
        mTexCoords = vbb3.asIntBuffer();
        mTexCoords.put(texCoords);
        mTexCoords.position(0);
        
        ByteBuffer vbb4 = ByteBuffer.allocateDirect(texCoords1.length*4);
        vbb4.order(ByteOrder.nativeOrder());
        mTexCoords1 = vbb4.asIntBuffer();
        mTexCoords1.put(texCoords1);
        mTexCoords1.position(0);
        
        mIndices1 = ByteBuffer.allocateDirect(indices1.length);
        mIndices1.put(indices1);
        mIndices1.position(0);
        
        mIndices2 = ByteBuffer.allocateDirect(indices2.length);
        mIndices2.put(indices2);
        mIndices2.position(0);
        
        mIndices3 = ByteBuffer.allocateDirect(indices3.length);
        mIndices3.put(indices3);
        mIndices3.position(0);
        
        mIndices4 = ByteBuffer.allocateDirect(indices4.length);
        mIndices4.put(indices4);
        mIndices4.position(0);
        
        mIndices5 = ByteBuffer.allocateDirect(indices5.length);
        mIndices5.put(indices5);
        mIndices5.position(0);
        
        mIndices6 = ByteBuffer.allocateDirect(indices6.length);
        mIndices6.put(indices6);
        mIndices6.position(0);
        
        mIndices7 = ByteBuffer.allocateDirect(indices7.length);
        mIndices7.put(indices7);
        mIndices7.position(0);
		// �����Ļ����Ȼ���
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// ���õ�ǰ��ģ�͹۲����
		gl.glLoadIdentity();
		
		//�������GL_LIGHTING���ʲô��������
		//gl.glEnable(GL10.GL_LIGHTING);
		
		gl.glTranslatef(-0.0f, 0.0f, -5.0f);
		
		//������ת
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

		//ѡ��ʹ�õ�����
		 
		gl.glNormalPointer(GL10.GL_FIXED, 0, mNormals);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0,  mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTexCoords);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
	
		//�����ı���
		//gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24,  GL10.GL_UNSIGNED_BYTE, indices1);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, mIndices1);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 8,  GL10.GL_UNSIGNED_BYTE, mIndices2);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 12,  GL10.GL_UNSIGNED_BYTE, mIndices3);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 16,  GL10.GL_UNSIGNED_BYTE, mIndices4);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 20,  GL10.GL_UNSIGNED_BYTE, mIndices5);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24,  GL10.GL_UNSIGNED_BYTE, mIndices6);
		//������ɫ
		gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	    
	    
	    
	    
	    
	  //����
	    
	 // ���õ�ǰ��ģ�͹۲����
		gl.glLoadIdentity();
	    gl.glTranslatef(0.0f, 0.0f, -10.0f);	
		gl.glNormalPointer(GL10.GL_FIXED, 0, mNormals1);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0,  mVertexBuffer1);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0,  mTexCoords1);	
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[6]);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, mIndices7);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	    
	    
	    //��ת�Ƕ�
	    if ( key )
		{
		 
			xrot+=xspeed; 
			yrot+=yspeed;
	    	zrot+=zspeed;
			
		}
	    
    	if (!light)				// �ж��Ƿ�ʼ��Դ
		{
    		gl.glDisable(GL10.GL_LIGHT1);		// ����һ�Ź�Դ
		}
		else					// ����
		{
			gl.glEnable(GL10.GL_LIGHT1);		// ����һ�Ź�Դ
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		 

		float ratio = (float) width / height;
		//����OpenGL�����Ĵ�С
		gl.glViewport(0, 0, width, height);
		//����ͶӰ����
		gl.glMatrixMode(GL10.GL_PROJECTION);
		//����ͶӰ����
		gl.glLoadIdentity();
		// �����ӿڵĴ�С
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// ѡ��ģ�͹۲����
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		// ����ģ�͹۲����
		gl.glLoadIdentity();	
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//gl.glFrontFace();
		// ����ϵͳ��͸�ӽ�������
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		// ��ɫ����
		//gl.glClearColor(0, 0, 0, 0);
		gl.glClearColor(.2f, .4f, .4f, 1);
		gl.glEnable(GL10.GL_CULL_FACE);
		// ������Ӱƽ��
		gl.glShadeModel(GL10.GL_SMOOTH);
		// ������Ȳ���
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		
		IntBuffer textureBuffer = IntBuffer.allocate(7);
		// ��������
		gl.glGenTextures(7, textureBuffer);
		texture = textureBuffer.array();
		
		// ���� Nearest �˲���ͼ
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap1, 0);
		
		//GLImage.mBitmap1=null;
		

		


		
		// ���������˲�����
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap2, 0);
		
		//GLImage.mBitmap2=null;
		
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap3, 0);
		
		

		//GLImage.mBitmap3=null;
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap4, 0);
		
		//GLImage.mBitmap4=null;
		
		
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap5, 0);
		
		//GLImage.mBitmap5=null;
		
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap6, 0);
		
		//GLImage.mBitmap6=null;
		
		
		//����
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[6]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.wood, 0);
		
		//GLImage.wood=null;
		
		
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//���û�����
	   //gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);

	    //���������
	   // gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);

	    //���ù�Դ��λ��
	    //gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);
	    
	    //����һ�Ź�Դ
	   // gl.glEnable(GL10.GL_LIGHT1);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
            float x = event.values[SensorManager.DATA_X]; 
            
            float y = event.values[SensorManager.DATA_Y]; 

            float z = event.values[SensorManager.DATA_Z]; 

           

            //��ȡ��ǰʱ�̵ĺ����� 

            curTime = System.currentTimeMillis(); 



            //100������һ�� 

            if ((curTime - lastTime) > 100) 
            { 
                duration = (curTime - lastTime); 
                // ���ǲ��Ǹտ�ʼ�ζ� 
                if (last_x == 0.0f && last_y == 0.0f && last_z == 0.0f) 
                { 
                    //last_x��last_y��last_zͬʱΪ0ʱ����ʾ�ոտ�ʼ��¼ 
                    //initTime = System.currentTimeMillis(); 
                } 
                else 
                { 
                    // ���λζ���� 
                    shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math.abs(z - last_z)) / duration * 100; 
                } 
                //��ÿ�εĻζ������ӣ��õ�����ζ����                
                totalShake += shake;                
                // �ж��Ƿ�Ϊҡ��
                if (totalShake > 30) {
                	 
                  dicerock();
                  //SceneDice.play(123,0);
                  //rockflag=true;
                    
                }

                last_x = x; 

                last_y = y; 

                last_z = z; 

                lastTime = curTime; 
                totalShake=0;
            } 


		}
}



	

	public boolean onTouchEvent(MotionEvent e) {   
		        
		      switch (e.getAction()) {   
		      case MotionEvent.ACTION_DOWN:
		    	    //rockflag=true;
		    	 	dicerock(); 
		    	 	
		        }   		        
		        return true;   
		    }   

//骰子晃动
   public void dicerock()
   {
	    SceneDice.abc();
   	    key = true;
	    xspeed=-11f;
		yspeed=-11f;
		SceneDice.play(123,0);
		new CountDownTimer(3000,100) {

	     	   @Override
	     	   public void onTick(long millisUntilFinished) {
	     	    // TODO Auto-generated method stub
	     	    
	     	   }
	     	         @Override
	     	         public void onFinish() {
	     	        	
	     	        	//rockflag=false;
	     	        	if(activityflag==4)
	    	        	{
	    	        	Random r = new Random();
	            	    flag=r.nextInt(4)+1;
	    	        	if(flag==1)
	    	        	{
	    	        	 key=false;
	    	        	 xrot=45+13;
	    	        	 yrot=45;
	    	        	 zrot=0;
	    	        	 
	    	        	}
	    	        	if(flag==2)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+180+13;
	    	        		yrot=45;
	    	        	 zrot=0;
	    	        	}
	    	           
	    	        	if(flag==3)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        	if(flag==4)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32-180;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        }
	    	        	if(activityflag==5)
	    	        	{
	    	        	Random r = new Random();
	          	flag=r.nextInt(5)+1;
	    	        	if(flag==1)
	    	        	{
	    	        	 key=false;
	    	        	 xrot=45+13;
	    	        	 yrot=45;
	    	        	 zrot=0;
	    	        	 
	    	        	}
	    	        	if(flag==2)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+180+13;
	    	        		yrot=45;
	    	        	 zrot=0;
	    	        	}
	    	             if(flag==5)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+13;
	    	        		yrot=45;
	    	        		zrot=90;
	    	        		
	    	        	}
	    	        	if(flag==3)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        	if(flag==4)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32-180;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        	
	    	        }
	    	        	if(activityflag==1)
	    	        	{
	    	        	Random r = new Random();
	               	flag=r.nextInt(6)+1;
	    	        	if(flag==1)
	    	        	{
	    	        	 key=false;
	    	        	 xrot=45+13;
	    	        	 yrot=45;
	    	        	 zrot=0;
	    	        	 
	    	        	}
	    	        	if(flag==2)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+180+13;
	    	        		yrot=45;
	    	        	 zrot=0;
	    	        	}
	    	        	if(flag==3)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+13;
	    	        		yrot=45;
	    	        		zrot=90;
	    	        		
	    	        	}
	    	        	if(flag==4)
	    	        	{
	    	        		key=false;
	    	        		xrot=45+180+13;
	    	        		yrot=45;
	    	        		zrot=90;
	    	        	}
	    	        	if(flag==5)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        	if(flag==6)
	    	        	{
	    	        		key=false;
	    	        		xrot=-32-180;
	    	        		yrot=0;
	    	        		zrot=45;
	    	        	}
	    	        
	    	        }
	     	         }
	     	              }.start();
		   
       
     	
     	
   }
  
}
