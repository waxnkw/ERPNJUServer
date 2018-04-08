package blService.billService.approveBillBlService;

import java.rmi.RemoteException;
import java.util.ArrayList;

import vo.BillVO;
import vo.PurchaseReturnBillVO;
import vo.UserInfoVO;

public interface ApprovePurchaseReturnBillBlService {

	/**
	 * �õ�������δ���������嵥
	 * @throws RemoteException
	 * */
	public ArrayList<PurchaseReturnBillVO> getBillsList() throws RemoteException;
	
	/**
	 * ���ݵ��ݱ��ͨ����������
	 * @throws RemoteException
	 * */
	public boolean passBill(String billId,UserInfoVO approver,String approverComment) 
			throws RemoteException;
	 
	/**
	 * ͨ�����ݱ�žܾ���������
	 * @throws RemoteException
	 * */
	public boolean denyBill(String billId,UserInfoVO approver,String approverComment) 
			throws RemoteException;
}