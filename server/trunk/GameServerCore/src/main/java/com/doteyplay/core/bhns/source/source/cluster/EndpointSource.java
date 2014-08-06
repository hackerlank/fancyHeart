package com.doteyplay.core.bhns.source.source.cluster;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.bhns.IServicePortal;
import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.core.bhns.ServiceInfo;
import com.doteyplay.core.bhns.active.ActiveServiceHolder;
import com.doteyplay.core.bhns.data.ServiceData;
import com.doteyplay.core.bhns.event.IMultipleServiceEvent;
import com.doteyplay.core.bhns.location.ISvrLocationService;
import com.doteyplay.core.bhns.portal.AbstractLocalPortal;
import com.doteyplay.core.bhns.source.IEndpointSource;
import com.doteyplay.core.bhns.source.IServiceOption;
import com.doteyplay.core.bhns.source.options.ClusterServiceOption;

public class EndpointSource implements IEndpointSource
{
	private ClusterServiceOption serviceOption;
	private byte endpointId;
	private String configFile;
	private String dataBlocks;
	private String portalImplClassName;
	private Class<?> portalImplClass;
	private String serviceImplClassName;
	private Class<? extends ISimpleService> serviceImplClass;

	private AbstractLocalPortal<?> portalmpl;
	// ���ʱ�汾Ϊ0������ʱ++
	private AtomicInteger version = new AtomicInteger(0);

	public EndpointSource(ClusterServiceOption serviceOption, byte endpointId,
			String portalImplClassName, String serviceImplClassName,
			String configfile, String dataBlocks)
	{
		this.serviceOption = serviceOption;
		this.portalImplClassName = portalImplClassName;
		this.serviceImplClassName = serviceImplClassName;
		this.configFile = configfile;
		this.endpointId = endpointId;
		this.dataBlocks = dataBlocks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize()
	{
		try
		{
			this.portalImplClass = Class.forName(portalImplClassName);
			this.serviceImplClass = (Class<? extends ISimpleService>) Class
					.forName(serviceImplClassName);
			this.serviceImplClass.getConstructor(null).setAccessible(true);
			Constructor<?> tmpConstructor = portalImplClass.getConstructor(
					IServiceOption.class, IEndpointSource.class);
			this.portalmpl = (AbstractLocalPortal<?>) tmpConstructor
					.newInstance(serviceOption, this);

			this.portalmpl.initializePortal();
			this.portalmpl.initEventListerner();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean supportCluster()
	{
		return (serviceOption.configType() == IServiceOption.CONFIG_TYPE_CLUSTER);
	}

	@Override
	public boolean isLocalImplemention()
	{
		return true;
	}

	@Override
	public int getVersion()
	{
		return version.get();
	}

	@Override
	public String getConfigFile()
	{
		return this.configFile;
	}

	@Override
	public byte getEndpointId()
	{
		return endpointId;
	}

	@Override
	public int getPortalId()
	{
		return serviceOption.portalId();
	}

	@Override
	public String getDataBlocks()
	{
		return dataBlocks;
	}

	@Override
	public boolean isAvailable()
	{
		return (portalmpl != null);
	}

	@Override
	public IServicePortal<? extends ISimpleService> getServicePortalImpl()
	{
		return portalmpl;
	}

	@Override
	public AbstractSimpleService<?> createServiceImpInstance()
	{
		if (serviceImplClass != null)
		{
			try
			{
				return (AbstractSimpleService<?>) serviceImplClass
						.newInstance();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String postPortalCommand(int portalId, String requestCommand)
	{
		return this.portalmpl.doPortalCommand(portalId, requestCommand);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T findService(int portalId, long svrid)
	{
		return (T) this.portalmpl.findService(portalId, svrid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T getPortalService(int portalId)
	{
		return (T) portalmpl.getPortalService();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T activeService(long svrid, boolean isOrderActive)
	{	//�������Ľ�������.�����߼�.
		T tmpT = (T) portalmpl.activeService(svrid, isOrderActive);
		if (tmpT != null)// && this.supportCluster())
		{
			ServiceInfo info = BOServiceManager
					.getServiceInfo(ISvrLocationService.PORTAL_ID);
			for (byte endpointId : info.source().endpointIdList())
			{
				IEndpointSource source = info.source().getEndpoint(endpointId);
				ISimpleService service = source
						.getPortalService(ISvrLocationService.PORTAL_ID);
				if (service != null && service.isValid())
					((ISvrLocationService) service).changeLocationOfserviceId(
							serviceOption.portalId(), svrid, this.endpointId);
			}
			
			if(isOrderActive)
				ActiveServiceHolder.activeNextService(getPortalId(), svrid);
		}
		return tmpT;
	}

	@Override
	public boolean isActive(long svrid)
	{
		return portalmpl.isActive(svrid);
	}

	@Override
	public void destroyService(long svrid)
	{
		this.portalmpl.destroyService(svrid);
		// if (this.supportCluster())
		{
			ServiceInfo info = BOServiceManager
					.getServiceInfo(ISvrLocationService.PORTAL_ID);
			for (byte endpointId : info.source().endpointIdList())
			{

				IEndpointSource source = info.source().getEndpoint(endpointId);
				ISimpleService service = source
						.getPortalService(ISvrLocationService.PORTAL_ID);
				if (service != null && service.isValid())
					((ISvrLocationService) service).changeLocationOfserviceId(
							serviceOption.portalId(), svrid, (byte) -1);
			}
		}
	}

	@Override
	public boolean updateServiceImplClz(Class<? extends ISimpleService> clz)
	{
		if (clz == null)
			return false;
		this.serviceImplClass = clz;
		this.serviceImplClassName = clz.getName();
		try
		{
			this.serviceImplClass.getConstructor(null).setAccessible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		version.getAndAdd(1);
		return true;
	}

	public ServiceData getServiceData(long serviceId)
	{
		return this.portalmpl.getServiceData(serviceId);
	}

	@Override
	public Class<? extends ISimpleService> getServiceImplClz()
	{
		return serviceImplClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T findService(long svrid)
	{
		return (T) this.findService(this.serviceOption.portalId(), svrid);
	}

	@Override
	public boolean regMultipleEventListener(IMultipleServiceEvent listener)
	{
		this.portalmpl.regMultipleTrigger(listener);
		return true;
	}

	public void rmMultipleEventListener(int eventid, Object key)
	{
		this.portalmpl.rmMultipleTrigger(eventid, key);
	}

	@Override
	public void triggerEvent(int eventid, String methodSignature, Object[] args)
	{
		portalmpl.triggerEvent(eventid, methodSignature, args);
	}

	@Override
	public void triggerMultipleEvent(int eventid, String methodSignature,
			Object key, Object[] args)
	{
		portalmpl.triggerMultipleEvent(eventid, methodSignature, key, args);
	}

	@Override
	public boolean isSyncPortalCommand(int portalId, String requestCommand)
	{
		return portalmpl.isSyncPortalCommand(portalId, requestCommand);
	}
}