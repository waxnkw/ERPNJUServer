package blImpl.bill.approveBillBl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import assistant.convertors.FinanceBillsPOVOConvertor;
import assistant.convertors.UserInfoPOVOConvertor;
import assistant.type.BillStateEnum;
import assistant.utility.Date;
import blInteract.billBlInteract.BillBlInteractServiceFactory;
import blInteract.billBlInteract.ReceiptBillBlService;
import blInteract.financeInteract.BankAccountSumUpdateBlService;
import blInteract.financeInteract.FinanceBlFactory;
import blService.billService.approveBillBlService.ApproveReceiptBillBlService;
import po.ReceiptBillPO;
import po.UserInfoPO;
import vo.ReceiptBillVO;
import vo.UserInfoVO;

/**
 * 单据的审批，包括得到需要审批单据列表，和pass deny单据
 * @author 张傲  161250193
 * @version 2017.12.3
 */

public class ApproveReceiptBillBlController implements ApproveReceiptBillBlService{

    private ReceiptBillBlService service 
    	= new BillBlInteractServiceFactory().getReceiptBillBlService();//现金费用单相关操作
	
    /**
	 * 得到需要审批的单据列表
	 * @return ArrayList<ReceiptBillVO> 需要审批的单据列表
	 * @throws RemoteException
	 */
	@Override
	public ArrayList<ReceiptBillVO> getBillsList() throws RemoteException {
		ArrayList<ReceiptBillPO> list = service.getBillListByState(BillStateEnum.TBD);
		ArrayList<ReceiptBillVO> targetList =new ArrayList<ReceiptBillVO>();
		for(ReceiptBillPO po: list){
			ReceiptBillVO vo = FinanceBillsPOVOConvertor.receiptBillPOtoVO(po) ;
			targetList.add(vo);
		}
		return targetList;
	}

	  /**
	  * 根据Id通过单据的审批
	  * @param String billId单据的Id
	  * @param UserInfoVO approver 审批人信息
	  * @param String approverComment 审批人的批注 
	  * @throws RemoteException
	  */
	@Override
	public boolean passBill(String billId, UserInfoVO approver, String approverComment) throws RemoteException {
		ReceiptBillPO billPO = service.getBillById(billId);
		UserInfoPO approverPO = UserInfoPOVOConvertor.voToPO(approver);
		billPO.setApprover(approverPO);
		billPO.setApproveDate(new Date());
		billPO.setApproverComment(approverComment);
		service.passBill(billPO);
		
		//修改银行账户
		BankAccountSumUpdateBlService bankAccountSumUpdateBlService= new FinanceBlFactory().getBankAccountSumUpdateBlService();
		bankAccountSumUpdateBlService.updateRemainingSum(billPO.getBankAccount(),-billPO.getSum());
		return true;
	}

	 /**
	  * 根据Id拒绝通过单据的审批
	  * @param String billId单据的Id
	  * @param UserInfoVO approver 审批人信息
	  * @param String approverComment 审批人的批注 
	  * @throws RemoteException
	  */
	@Override
	public boolean denyBill(String billId, UserInfoVO approver, String approverComment) throws RemoteException {
		ReceiptBillPO billPO = service.getBillById(billId);
		UserInfoPO approverPO = UserInfoPOVOConvertor.voToPO(approver);
		billPO.setApprover(approverPO);
		billPO.setApproveDate(new Date());
		billPO.setApproverComment(approverComment);
		service.denyBill(billPO);
		return true;
	}

}
