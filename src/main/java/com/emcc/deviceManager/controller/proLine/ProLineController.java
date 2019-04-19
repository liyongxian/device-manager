package com.emcc.deviceManager.controller.proLine;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.emcc.deviceManager.common.Constans;
import com.emcc.deviceManager.common.Tools;
import com.emcc.deviceManager.controller.QueryController;
import com.emcc.deviceManager.exception.CheckDefineException;
import com.emcc.deviceManager.param.proline.EditProline;
import com.emcc.deviceManager.param.proline.NewProline;
import com.emcc.deviceManager.param.proline.Proline;
import com.emcc.deviceManager.paramcheck.proline.CheckProline;
import com.emcc.deviceManager.paramcheck.user.CheckUser;
import com.emcc.deviceManager.response.BaseResponse;
import com.emcc.deviceManager.response.model.NodeRespones;
import com.emcc.deviceManager.response.proline.ProlineResponse;
import com.emcc.deviceManager.service.proline.ProlineService;

@Api(tags = { "产线接口" })
@RestController
@RequestMapping(value = "/proLine")
public class ProLineController {
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
    private CheckProline checkProline;
	@Autowired
    private ProlineService prolineService;
	@Autowired
    private CheckUser checkUser;
	
	private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

	@ApiOperation(value = "新增产线", notes = "新增产线", produces = "application/json")
	@RequestMapping(value = "/add/{tenantId}", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse getNodesByCondition(
			@ApiParam(value = "租户id") 
				@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "新增产线参数") 
			@RequestBody NewProline proline) {
		ProlineResponse response=new ProlineResponse();
		
		//调用model接口存入数据
		try{
			checkProline.checkAddProline(proline);	
			String uri=prolineService.addProlineold(tenantId, proline);
			response.setSuccess(true);
			response.setMessage("操作成功");
			response.setUri(uri);
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "新增产线", notes = "新增产线", produces = "application/json")
	@RequestMapping(value = "/add/{tenantId}/{userId}", method = RequestMethod.POST)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse addProline(
			@ApiParam(value = "租户id") 
				@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "用户id") 
				@PathVariable("userId") String userId,
			@ApiParam(value = "新增产线参数") 
			@RequestBody NewProline proline) {
		ProlineResponse response=new ProlineResponse();
		
		//调用model接口存入数据
		try{			
			checkProline.checkAddProline(proline);	
			//判断用户是否存在，不存在则创建
			checkUser.checkUser(userId);
			String uri=prolineService.addProline(tenantId,userId, proline);
			response.setSuccess(true);
			response.setMessage("操作成功");
			response.setUri(uri);
		}catch(Exception e){
			response.setMessage("查询异常"+e);
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "单个产线查询", notes = "单个产线查询,产线编辑时使用", produces = "application/json")
	@RequestMapping(value = "/query/{tenantId}/{prolineId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse getProlineByID(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "产线Id，产线uri第二个/后面的部分") 
				@PathVariable("prolineId") String prolineId) {
		ProlineResponse response=new ProlineResponse();		
		
		try{
			Tools.checkUriFormat(Tools.generateUri(tenantId, prolineId));
			Proline proline=prolineService.queryProlineByUri(tenantId,prolineId);
			response.setProline(proline);
			response.setSuccess(true);
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}			
		return response;
	}
	@ApiOperation(value = "编辑产线", notes = "编辑产线", produces = "application/json")
	@RequestMapping(value = "/edit/{tenantId}", method = RequestMethod.PUT)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse edit(@ApiParam(value = "租户id") 
		    @PathVariable("tenantId") String tenantId,
			@ApiParam(value = "编辑产线参数，请确定包含产线id以及其他属性") 
			@RequestBody EditProline proline) {
		ProlineResponse response=new ProlineResponse();	
	
		//调用model接口存入数据
		try{
			checkProline.checkeditProline(proline);
			boolean result=prolineService.editPatchProline(tenantId,proline);
			response.setSuccess(result);
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}
		return response;
	}
	@ApiOperation(value = "查询产线树", notes = "查询产线树", produces = "application/json")
	@RequestMapping(value = "/queryProlineTree/{tenantId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse getProlines(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId) {
		ProlineResponse response=new ProlineResponse();		
		try{
			NodeRespones nodeRespones=prolineService.queryProlineByNamespaceold(tenantId);
			response.setMessage(nodeRespones.getMsg());
			response.setSuccess(true);
			response.setResult(nodeRespones.getResult());
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "查询产线树", notes = "查询产线树", produces = "application/json")
	@RequestMapping(value = "/queryProlineTree/{tenantId}/{userId}", method = RequestMethod.GET)
	//@Cacheable(value="v2-getNodesByCondition",keyGenerator="keyGenerator",unless="#result.getCode() != '0x00000000'")
	public ProlineResponse getProlines(
			@ApiParam(value = "租户id") 
			@PathVariable("tenantId") String tenantId,
			@ApiParam(value = "用户id") 
			@PathVariable("userId") String userId) {
		ProlineResponse response=new ProlineResponse();		
		try{
			NodeRespones nodeRespones=prolineService.queryProlineByNamespace(tenantId,userId);
			response.setMessage(nodeRespones.getMsg());
			response.setSuccess(true);
			response.setResult(nodeRespones.getResult());
		}catch(Exception e){
			response.setMessage(e.toString());
			response.setSuccess(false);
		}		
		return response;
	}
	@ApiOperation(value = "删除产线", notes = "删除产线", produces = "application/json")
	@RequestMapping(value = "/deleteProline/{tenantId}/{prolineId}", method = RequestMethod.DELETE)
	public BaseResponse deleteProlines(
			@ApiParam(value = "租户id,相当于namespace") @PathVariable(value = "tenantId") String tenantId,
			@ApiParam(value = "产线id") @PathVariable(value = "prolineId") String prolineId) {
		BaseResponse response = new BaseResponse();
		try {
			prolineService.deleteProline(tenantId, prolineId);
			response.setMessage("删除成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage(e.toString());
			response.setSuccess(false);
			return response;
		}
		return response;
	}
}
