package ui.promotionUi.updatePromotionUi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import assistant.exception.Myexception;
import assistant.utility.Date;
import blService.promotionBlService.TotalStrategyBlService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import rmi.remoteHelper.RemoteHelperFactory;
import ui.promotionUi.PromotionUiController;
import ui.promotionUi.bLobj.Goods;
import ui.uiAssistants.InputErrorMessageEnum;
import vo.GiftVO;
import vo.GoodsVO;
import vo.TotalStrategyVO;


public class UpdateTotalPromotionUiController implements PromotionUiForGiftsUiController{
	private PromotionUiController promotionUiController;//��������controller������
	private TotalStrategyVO totalStrategyVO = new TotalStrategyVO();//�˽���Ҫ��ʾ��userstrategy��Ϣ
	
	private HashMap<GiftVO, Goods> giftsGoodsMap = new HashMap<>();
	private ObservableList<GiftVO> gifts = FXCollections.observableArrayList();

	/***************************************************************************
     *                                                                         *
     * ������ʾԪ��                                                     *
     *                                                                         *
     **************************************************************************/
	//������ʾ��textfield
	@FXML
	private TextField nameTextField ;
	@FXML
	private TextField idTextField;	    
	@FXML
	private TextField totalConsumptionTextField;
	@FXML
	private TextField startYearTextField;
	@FXML
	private TextField startMonthTextField;
	@FXML
	private TextField startDayTextField;
	@FXML
	private TextField endYearTextField;
	@FXML
	private TextField endMonthTextField;
	@FXML
	private TextField endDayTextField;
	@FXML
	private TextField discountTextField;	
	@FXML
	private TextField couponTextField ;
	@FXML
	private Text totalPriceText;
	@FXML
	private TextArea remarkTextArea;
	
	//���ý����Ԥ������ʱ���
	private void setTodayPromptText(){
		Date today = new Date();
		String todayString = today.getYMDDate();
		String[] ymd = todayString.split("\\.");
		startYearTextField.setPromptText(ymd[0]);
		startMonthTextField.setPromptText(ymd[1]);
		startDayTextField.setPromptText(ymd[2]);
		endYearTextField.setPromptText(ymd[0]);
		endMonthTextField.setPromptText(ymd[1]);
		endDayTextField.setPromptText(ymd[2]);
	}
	
	/****************************************************************************************/	
	
	/***************************************************************************
     *                                                                         *
     * ���������ʾ                                                    *
     *                                                                         *
     **************************************************************************/
	//������ʾ
	@FXML
	private Text totalConsumptionErrorText;
	@FXML
	private Text startDateErrorText;
	@FXML
	private Text endDateErrorText;
	@FXML
	private Text nameErrorText;
	@FXML
	private Text couponErrorText;
	@FXML
	private Text discountErrorText;
	

	private boolean checkInput(){
		if(!checkNameTextField()){return false;}
			
		if(!checkTotalConsumptionTextField()){ return false;}
		
		if(!checkStartDateTextField()){return false;}
		
		if(!checkEndDateTextField()){return false;}
		
		if(!checkDiscountTextField()){return false;}
		
		if(!checkCouponTextField()){return false;}
		
		return true;
	}
	
	private boolean checkNameTextField(){
		String name = nameTextField.getText();
		if(name == null||name.trim().equals("")){
			nameTextField.setText("������");
			return false;
		}
		return true;
	}
	private boolean checkTotalConsumptionTextField(){
		try{
			if(totalConsumptionTextField.getText()==null){
				//TODO
				totalConsumptionErrorText.setText(InputErrorMessageEnum.Uncompleted_Error.getErrorMessage());
				return false;
			}
			int coupon = Integer.parseInt(totalConsumptionTextField.getText());
		}catch(NumberFormatException e){
			//TODO
			totalConsumptionErrorText.setText(InputErrorMessageEnum.Number_Format_Error.getErrorMessage());
			return false;
		}
		couponErrorText.setText("");
		return true;
	}
	
	private boolean checkStartDateTextField(){
		try{
			if(startYearTextField.getText()==null||startMonthTextField.getText()==null
					||startDayTextField.getText()==null){
				//TODO
				startDateErrorText.setText(InputErrorMessageEnum.Uncompleted_Error.getErrorMessage());
				return false;
			}
			int year = Integer.parseInt(startYearTextField.getText());
			int month = Integer.parseInt(startMonthTextField.getText());
			int day = Integer.parseInt(startDayTextField.getText());
			Date startDate= new Date(year,month,day,00,00);
			totalStrategyVO.setStartDate(startDate);
		}catch (NumberFormatException e) {
			// TODO: handle exception
			startDateErrorText.setText(InputErrorMessageEnum.Time_Format_Error.getErrorMessage());
			return false;
		}
		startDateErrorText.setText("");
		return true;
	}
	
	private boolean checkEndDateTextField(){
		try{
			if(endYearTextField.getText()==null||endMonthTextField.getText()==null
					||endDayTextField.getText()==null){
				//TODO
				endDateErrorText.setText(InputErrorMessageEnum.Uncompleted_Error.getErrorMessage());
				return false;
			}
			int year = Integer.parseInt(endYearTextField.getText());
			int month = Integer.parseInt(endMonthTextField.getText());
			int  day = Integer.parseInt(endDayTextField.getText());
			Date endDate = new Date(year,month,day,00,00);
			if(endDate.compareTo(totalStrategyVO.getStartDate())<=0){
				//TODO ʱ���ص�
				endDateErrorText.setText(InputErrorMessageEnum.Time_End_Less_Than_Start.getErrorMessage());
				return false;
			}
		}catch(NumberFormatException e){
			//TODO 
			endDateErrorText.setText(InputErrorMessageEnum.Time_Format_Error.getErrorMessage());
			return false;
		}
		endDateErrorText.setText("");
		return true;
	}
	
	private boolean checkDiscountTextField(){
		try{
			String discountString = discountTextField.getText();
			
			if(discountString==null||discountString.trim().equals("")){
				discountString = "100";
			}
			double discount = Double.parseDouble(discountString)/100.0;
			if(discount>1||discount<0){
				//TODO
				discountErrorText.setText(InputErrorMessageEnum.Number_Out_Of_Bounds.getErrorMessage());
				return false;
			}
		}catch (NumberFormatException e) {
			// TODO: handle exception
			discountErrorText.setText(InputErrorMessageEnum.Number_Format_Error.getErrorMessage());
			return false;
		}
		return true;
	}
	
	private boolean checkCouponTextField(){
		try{
			if(couponTextField.getText()==null){
				//TODO
				couponErrorText.setText(InputErrorMessageEnum.Uncompleted_Error.getErrorMessage());
				return false;
			}
			int coupon = Integer.parseInt(couponTextField.getText());
		}catch(NumberFormatException e){
			//TODO
			couponErrorText.setText(InputErrorMessageEnum.Number_Format_Error.getErrorMessage());
			return false;
		}
		couponErrorText.setText("");
		return true;
	}
	
	/****************************************************************************************/
	
	
	/***************************************************************************
     *                                                                         *
     * ����Ʒ��������ӿ�                                                   *
     *                                                                         *
     **************************************************************************/
	//��Ʒ��Ϣ�嵥
	TotalStrategyBlService totalStrategyBlService 
		= RemoteHelperFactory.getPromotionRemoteHelper().getTotalStrategyBlService();// �û��������Ե�bl
	ObservableList<Goods> goodsList = FXCollections.observableArrayList();//ά�����е���Ʒ��Ϣ
	//��ʼ��
	private void initGoodsList(){
		try {
			ArrayList<GoodsVO> vos = totalStrategyBlService.getGoodsList();
			for(GoodsVO vo:vos){
				goodsList.add(new Goods(vo));
			}
		}catch (Myexception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	//��������Ʒ�õ������ӿ�
	@Override
	public ObservableList<Goods> getGoodsList(){
		return goodsList;
	}
	@Override
	public void addGifts(ObservableList<GiftVO> vos , HashMap<GiftVO, Goods> map){
		giftsGoodsMap.putAll(map);
		gifts.addAll(vos);
	}
	
	/****************************************************************************************/

	
	/***************************************************************************
     *                                                                         *
     * ҳ����ȷ��ȡ����ť������                                                   *
     *                                                                         *
     **************************************************************************/
	@FXML
	protected void addGiftButtonListener(Event event){
		UpdateGiftUiStarter starter = new UpdateGiftUiStarter(this);
		starter.start();
	}
	
	//ȷ��ȡ����ť
	@FXML
	protected void ensureButtonListener(Event event){
		boolean isSetComplemented= checkInput();
		if(!isSetComplemented){
			return;
		}
		generateUserStrategyVo();
		promotionUiController.updateTotalStrategy(totalStrategyVO);
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	@FXML
	protected void cancelButtonListener(Event event){
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	
	/**
	 * ����������Ϣ�����û���������
	 * */
	private void generateUserStrategyVo(){
		String name = nameTextField.getText();
		totalStrategyVO.setName(name);
		
		double totalCosumption =Double.parseDouble( totalConsumptionTextField.getText());
		totalStrategyVO.setTotalConsumption(totalCosumption);
		
		int year = Integer.parseInt(startYearTextField.getText());
		int month = Integer.parseInt(startMonthTextField.getText());
		int day = Integer.parseInt(startDayTextField.getText());
		Date startDate= new Date(year,month,day,00,00);
		totalStrategyVO.setStartDate(startDate);
	
		year = Integer.parseInt(endYearTextField.getText());
		month = Integer.parseInt(endMonthTextField.getText());
		day = Integer.parseInt(endDayTextField.getText());
		Date endDate = new Date(year,month,day,00,00);
		totalStrategyVO.setEndDate(endDate);
		
		double discount = Double.parseDouble(discountTextField.getText())/100.0;
		totalStrategyVO.setDiscount(discount);
		
		int coupon = Integer.parseInt(couponTextField.getText());
		totalStrategyVO.setCoupon(coupon);
		
		String remark = remarkTextArea.getText();
		totalStrategyVO.setRemark(remark);
		
		ArrayList<GiftVO> giftVOs = new ArrayList<>();
		giftVOs.addAll(gifts);
		totalStrategyVO.setGifts(giftVOs);
	}
	
	/****************************************************************************************/

	
	/***************************************************************************
     *                                                                         *
     * controller�ĳ�ʼ����Ԥ������                                                *
     *                                                                         *
     **************************************************************************/
	protected void init(){
		initGoodsList();
		initGiftsList();
		initGiftsGoodsMap();
		setTodayPromptText();
		setGiftsTableView();
		if(totalStrategyVO!=null){
			setTotalText();
		}
	}

	protected void setTotalStrategyVO(TotalStrategyVO vo){
		this.totalStrategyVO = vo;
	}
	
	protected void setTotalText(){
		nameTextField.setText(totalStrategyVO.getName());
		
		Date startDate= totalStrategyVO.getStartDate();
		String startDateString = startDate.getYMDDate();
		String[] ymd = startDateString.split("\\.");
		startYearTextField.setText(ymd[0]);
		startMonthTextField.setText(ymd[1]);
		startDayTextField.setText(ymd[2]);
		
		Date endDate =totalStrategyVO.getEndDate();
		String endDateString = endDate.getYMDDate();
		ymd = endDateString.split("\\.");
		endYearTextField.setText(ymd[0]);
		endMonthTextField.setText(ymd[1]);
		endDayTextField.setText(ymd[2]);
		
		discountTextField.setText(totalStrategyVO.getDiscount()*100+"");
		
		double totalPrice=0;
		for(GiftVO gift: gifts){
			totalPrice += gift.getTotalPrice();
		}	
		totalPriceText.setText(totalPrice+"");
		
		totalConsumptionTextField.setText(totalStrategyVO.getTotalConsumption()+"");
		
		couponTextField.setText(totalStrategyVO.getCoupon()+"");
		
		remarkTextArea.setText(totalStrategyVO.getRemark());

		giftTableView.refresh();
	}
	
	private void initGiftsList(){
		gifts.addAll(totalStrategyVO.getGifts());
	}
	
	private void initGiftsGoodsMap(){
		for(GiftVO gift: gifts){
			for(Goods goods:goodsList){
				if(gift.getId()==goods.getId()){
					goods.setAddedToGift(true);
					giftsGoodsMap.put(gift, goods);
				}
			}
		}
	}
	
	protected void setPromotionUiController(PromotionUiController promotionUiController){
		this.promotionUiController = promotionUiController;
	}
	/****************************************************************************************/
	
	
	/***************************************************************************
     *                                                                         *
     * ��Ʒtablecolumn����ʾ                                                      *
     *                                                                         *
     **************************************************************************/
	//��Ʒ����ϢtableView
	@FXML
	private TableView<GiftVO> giftTableView;
	@FXML
	private TableColumn<GiftVO, String> giftIdTableColumn;
	@FXML
	private TableColumn<GiftVO, String> giftNameTableColumn;
	@FXML
	private TableColumn<GiftVO,Number> giftPriceTableColumn;
	@FXML
	private TableColumn<GiftVO,Number> giftAmountTableColumn;
	@FXML
	private TableColumn<GiftVO,Number> giftTotalPriceTableColumn;
	@FXML
	private TableColumn<GiftVO, Button> giftShowTableColumn;
	@FXML
	private TableColumn<GiftVO, Button> giftDeleteTableColumn;

	private void setGiftsTableView(){
		setGiftNameTableColumn();
		
		setGiftIdTableColumn();
	
		setGiftAmountTableColumn();
		
		setGiftPriceTableColumn();
		
		setGiftTotalPriceTableColumn();
		
		setGiftShowTableColumn();
		
		setGiftDeleteTableColumn();
		
		giftTableView.setItems(gifts);
	}
	
	private void setGiftNameTableColumn(){
		giftNameTableColumn.setCellValueFactory(cellDataFeatures->{
			GiftVO vo = cellDataFeatures.getValue();
			String name = vo.getName();
			ObservableValue<String> giftName = new SimpleStringProperty(name);
			return giftName;
		});
	}
	
	private void setGiftIdTableColumn(){
		giftIdTableColumn.setCellValueFactory(cellDataFeatures->{
			GiftVO vo = cellDataFeatures.getValue();
			String id = vo.getId();
			ObservableValue<String> giftId = new SimpleStringProperty(id);
			return giftId;
		});
	}
	
	private void setGiftAmountTableColumn(){
		giftTableView.setEditable(true);
		
		giftAmountTableColumn.setCellValueFactory(cellDataFeatures->{
			int amount= cellDataFeatures.getValue().getGiftAmount();
			ObservableValue<Number> giftAmount = new SimpleIntegerProperty(amount);
			return giftAmount;
		});
		
		giftAmountTableColumn.setCellFactory(col->{
			TextFieldTableCell<GiftVO, Number> cell = new TextFieldTableCell<GiftVO, Number>(new NumberStringConverter()){
				@Override
				public void commitEdit(Number n){
					super.commitEdit(n);
					int i= this.getIndex();
					long giftAmount = (long)n;
					if(i<gifts.size()){
						gifts.get(i).setGiftAmount((int)giftAmount);
						//������Ʒ�ܼ�
						double totalPrice = 0;
						for(int j=0;j<gifts.size();j++){
							totalPrice += gifts.get(j).getTotalPrice();
						}
						totalPriceText.setText(totalPrice+"");
						giftTableView.refresh();
						return;
					}
				}
			};

			cell.setEditable(true);
			return cell;
		});
		giftAmountTableColumn.setEditable(true);
	}
	
	private void setGiftPriceTableColumn(){
		giftPriceTableColumn.setCellValueFactory(cellDataFeatures->{
			GiftVO vo = cellDataFeatures.getValue();
			double price = vo.getPrice();
			ObservableValue<Number> giftPrice = new SimpleDoubleProperty(price);
			return giftPrice;
		});
	}

	private void setGiftTotalPriceTableColumn(){
		giftTotalPriceTableColumn.setCellValueFactory(cellDataFeatures->{
			GiftVO vo = cellDataFeatures.getValue();
			double price = vo.getTotalPrice();
			ObservableValue<Number> giftPrice = new SimpleDoubleProperty(price);
			return giftPrice;
		});
	}
	
	private void setGiftShowTableColumn(){
		giftShowTableColumn.setCellValueFactory(cellDataFeatures->{
			Button button = new Button("�鿴");
			button.setOnMouseClicked(e->{
				UpdateGiftUiStarter starter = new UpdateGiftUiStarter(this);
				starter.startAndShowGoods(cellDataFeatures.getValue());
			});
			ObservableValue<Button> btn = new SimpleObjectProperty(button);
			return btn;
		});
	}
	
	private void setGiftDeleteTableColumn(){
		giftDeleteTableColumn.setCellValueFactory(cellDataFeatures->{
			Button button = new Button("ɾ��");
			button.setOnMouseClicked(e->{
				GiftVO giftVO = cellDataFeatures.getValue();
				Goods goods=giftsGoodsMap.get(giftVO);
				goods.setAddedToGift(false);
				gifts.remove(giftVO);
			});
			ObservableValue<Button> btn = new SimpleObjectProperty(button);
			return btn;
		});
	}


}