INSERT INTO posts (post_code,post_name,post_address)
	VALUES (100100,'postName1','postAddress1');
INSERT INTO posts (post_code,post_name,post_address)
	VALUES (200200,'postName2','postAddress');
INSERT INTO posts (post_code,post_name,post_address)
	VALUES (300300,'postName3','postAddres3');
INSERT INTO posts (post_code,post_name,post_address)
	VALUES (400400,'postName4','postAddres4');
INSERT INTO posts (post_code,post_name,post_address)
	VALUES (500500,'postName5','postAddres5');

INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('LETTER',500500,'receiverAddress_aaa','receiverName_ccc');
INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('PARCEL',500500,'receiverAddress_abc','receiverName_abc');
INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('WRAPPER',200200,'receiverAddress_aab','receiverName_aba');
INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('LETTER',300300,'receiverAddress_bcc','receiverName_ccc');
INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('LETTER',500500,'receiverAddress_aaa','receiverName_ddd');
INSERT INTO public.items (item_type,post_code,receiver_address,receiver_name)
	VALUES ('PARCEL',100100,'receiverAddress_bbb','receiverName_abc');

INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,100100,'REGISTER','2023-08-21 21:21:50.894');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,200200,'REGISTER','2023-08-21 21:22:20.125');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (3,100100,'REGISTER','2023-08-21 21:22:54.346');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,500500,'REGISTER','2023-08-21 21:23:14.535');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,300300,'REGISTER','2023-08-21 21:24:09.911');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,300300,'REGISTER','2023-08-21 21:25:16.161');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,100100,'DEPART','2023-08-21 21:28:01.275');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,200200,'ARRIVE','2023-08-21 21:28:08.496');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,200200,'DEPART','2023-08-21 21:28:16.837');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,300300,'ARRIVE','2023-08-21 21:28:25.815');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,300300,'DEPART','2023-08-21 21:28:33.197');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,400400,'ARRIVE','2023-08-21 21:29:07.054');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,400400,'DEPART','2023-08-21 21:29:14.838');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,500500,'ARRIVE','2023-08-21 21:29:20.881');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (1,500500,'HAND_OVER','2023-08-21 21:30:07.616');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,200200,'DEPART','2023-08-21 21:31:53.553');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,300300,'ARRIVE','2023-08-21 21:33:43.111');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,300300,'DEPART','2023-08-21 21:35:53.553');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,400400,'ARRIVE','2023-08-21 21:36:24.842');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,400400,'DEPART','2023-08-21 21:36:34.165');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,500500,'ARRIVE','2023-08-21 21:36:41.251');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (2,500500,'HAND_OVER','2023-08-21 21:36:49.072');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (3,100100,'DEPART','2023-08-21 21:37:26.356');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (3,200200,'ARRIVE','2023-08-21 21:38:26.356');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (3,200200,'HAND_OVER','2023-08-21 21:38:57.891');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,500500,'DEPART','2023-08-21 21:46:36.069');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,400400,'ARRIVE','2023-08-21 21:47:23.971');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,400400,'DEPART','2023-08-21 21:47:30.807');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,300300,'ARRIVE','2023-08-21 21:47:38.635');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (4,300300,'HAND_OVER','2023-08-21 21:53:13.218');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,300300,'DEPART','2023-08-21 21:53:59.450');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,400400,'ARRIVE','2023-08-21 21:54:26.673');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,400400,'DEPART','2023-08-21 21:54:43.120');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,500500,'ARRIVE','2023-08-21 21:54:59.095');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (5,500500,'HAND_OVER','2023-08-21 21:55:12.021');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,300300,'DEPART','2023-08-21 21:56:20.945');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,200200,'ARRIVE','2023-08-21 21:56:52.335');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,200200,'DEPART','2023-08-21 21:57:13.174');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,100100,'ARRIVE','2023-08-21 21:57:20.603');
INSERT INTO public.operations (item_id,post_code,type,operated_on)
	VALUES (6,100100,'HAND_OVER','2023-08-21 21:57:31.205');

