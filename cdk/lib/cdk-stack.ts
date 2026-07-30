import { CfnEIP, CfnInternetGateway, CfnNatGateway, CfnRoute, CfnVPCGatewayAttachment, SubnetType, Vpc } from 'aws-cdk-lib/aws-ec2';
import * as cdk from 'aws-cdk-lib/core';
import { Construct } from 'constructs';
// import * as sqs from 'aws-cdk-lib/aws-sqs';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

   const vpc = new Vpc(this, "myVPC", {// this is for creating a VPC with 2 public and 2 private subnets
      vpcName: "expenseTracker",
      cidr: "10.0.0.0/16",
      maxAzs: 2,
      natGateways: 2,
      createInternetGateway: false,
      subnetConfiguration: [
        {
          cidrMask: 24,
          name: "public-subnet",
          subnetType: SubnetType.PUBLIC
        },
        {
          cidrMask: 24,
          name: "private-subnet",
          subnetType: SubnetType.PRIVATE_WITH_EGRESS
        }
      ]
    })
    // this is for creating an internet gateway and attaching it to the VPC so that the public subnets can access the internet
        const internetGateway = new CfnInternetGateway(this, "InternetGateway");
    new CfnVPCGatewayAttachment(this, "MyUniqueVPCGatewayAttachmet", {
      vpcId: vpc.vpcId,
      internetGatewayId: internetGateway.ref
    });
// this is for creating two NAT gateways in the public subnets so that the private subnets can access the internet
// Nat is always created in the public subnets and the private subnets route their traffic to the NAT gateways for internet access
    const natGatewayOne = new CfnNatGateway(this, "NatGatewayOne", {
      subnetId: vpc.publicSubnets[0].subnetId,
      allocationId: new CfnEIP(this, 'EIPForNatGatewayOne').attrAllocationId
    })

    const natGatewayTwo = new CfnNatGateway(this, "NatGatewayTwo", {
      subnetId: vpc.publicSubnets[1].subnetId,
      allocationId: new CfnEIP(this, 'EIPForNatGatewayTwo').attrAllocationId
    })
    // this is for creating routes in the private subnets to route their traffic to the NAT gateways for internet access
      vpc.privateSubnets.forEach((subnet, index) => {
    new CfnRoute(this, `PrivateRouteToNatGateway-${index}`, {
      routeTableId: subnet.routeTable.routeTableId,
      destinationCidrBlock: '0.0.0.0/0',
      natGatewayId: index === 0 ? natGatewayOne.ref : natGatewayTwo.ref
    })
  })
// this is for creating routes in the public subnets to route their traffic to the internet gateway for internet access
  vpc.publicSubnets.forEach((subnet, index) => {
    new CfnRoute(this, `PublicRouteToInternetGateway-${index}`, {
      routeTableId: subnet.routeTable.routeTableId,
      destinationCidrBlock: '0.0.0.0/0',
      gatewayId: internetGateway.ref
    })
  })

        new cdk.CfnOutput(this, 'VPCIdOutput', {
      value: vpc.vpcId,
      exportName: 'VpcId'
    })

    vpc.publicSubnets.forEach((subnet, index) => {
      new cdk.CfnOutput(this, `PublicSubnetOutput-${index}`, {
        value: subnet.subnetId,
        exportName: `PublicSubnet-${index}`
      });
    })

    vpc.privateSubnets.forEach((subnet, index) => {
      new cdk.CfnOutput(this, `PrivateSubnetOutput-${index}`, {
        value: subnet.subnetId,
        exportName: `PrivateSubnet-${index}`
      });
    })
  }
}
