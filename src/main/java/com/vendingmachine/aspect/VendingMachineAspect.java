package com.vendingmachine.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.vendingmachine.backend.vo.ErrorMsg;
import com.vendingmachine.exception.InsufficientBalanceException;
import com.vendingmachine.exception.LoginErrorException;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.exception.TimeFormatException;

@Component
@Aspect
public class VendingMachineAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//統一例外處理
	@Pointcut(value = "execution(* com.vendingmachine.backend.controller.*.*(..))")
	public void backendControllerpt() {/* 設定切入點 */}
	
	@Pointcut(value = "execution(* com.vendingmachine.frontend.controller.*.*(..))")
	public void frontendControllerpt() {/* 設定切入點 */}
    
	/**
	 * Exception處理
	 * @param proceedingJoinPoint
	 * @return
	 */
	@Around(value = "backendControllerpt() || frontendControllerpt()")
	public Object sessionAndExceptionCont(ProceedingJoinPoint proceedingJoinPoint) {
		String methodName = proceedingJoinPoint.getSignature().getName();
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
		} catch (LoginErrorException e) {
			logger.error(e.getMessage(), e);
			//createLog(e.getMessage(), e.getCode().toString(), methodName, "2", "3", e);
			result = new ResponseEntity<ErrorMsg>(new ErrorMsg( e.getCode(), "E01001" , e.getMessage()), HttpStatus.NOT_FOUND);
		} catch (QueryNoDataException e) {
			//logger.error(e.getMessage(), e);
			//createLog(e.getMessage(), e.getCode().toString(), methodName, "2", "3", e);
			System.out.println("QueryNoDataException");
			result = new ResponseEntity<ErrorMsg>(new ErrorMsg(e.getCode(), "E00002", e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (TimeFormatException e) {
			result = new ResponseEntity<ErrorMsg>(new ErrorMsg(e.getCode(), "E00003", e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (InsufficientBalanceException e) {
			result = new ResponseEntity<ErrorMsg>(new ErrorMsg(e.getCode(), "E00004", e.getMessage()), HttpStatus.BAD_REQUEST);
		}catch (Throwable e) {
			logger.error("sessionAndExceptionCont ERROR", e);
			System.out.println("Throwable");
			//createLog(e.getMessage(), "500", methodName, "2", "1", e);
			result = new ResponseEntity<ErrorMsg>(new ErrorMsg(500, "E00001",  "系統錯誤!!! <BR>" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	
	
	/*public void createLog(String data, String returnCode, String methodName, 
			   String sendStatus, String logType, Throwable e) {
		User user = (User) session.getAttribute("user");
		String userid = user == null ? "" : user.getAccountId().toString();
		
		ApiGuiLog apiGuiLog = new ApiGuiLog(userid, data, sendStatus, returnCode,
			methodName, logType, new Date());
		
		String outLog = "";
		if(sendStatus.equals("1")) {
			outLog ="request: " + apiGuiLog;
			if(!logType.equals("4")) {
				Integer userAccountId = user == null ? null : user.getAccountId();
				String userAccount = user == null ? "" : user.getAccount().toString();
				String userName = user == null ? "" : user.getName().toString();
				
				if(validateUtil.isNotNumNone(userAccountId)) {
					UserTrackLog userTrackLog = new UserTrackLog(userAccountId, userAccount, 
									userName, data, methodName, new Date());
					userTrackLogService.save(userTrackLog);
				}
			}
		} else {
			outLog = "response: " + apiGuiLog;
		}
		switch(logType) {
		case "1":
			if(logger.isErrorEnabled()) {
				logger.error(outLog, e);
			}
			break;
		case "2":
			if(logger.isWarnEnabled()) {
				logger.warn(outLog);
			}
			break;
		case "3":
			if(logger.isInfoEnabled()) {
				logger.info(outLog);
			}
			break;
		case "4":
			if(logger.isDebugEnabled()) {
				logger.debug(outLog);
			}
			break;
		}
		apiGuiLogService.save(apiGuiLog);
	}*/
	
}
